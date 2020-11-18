package com.eceplatform.QAForum.service.impl;

import com.eceplatform.QAForum.dto.QuestionRequest;
import com.eceplatform.QAForum.model.Question;
import com.eceplatform.QAForum.model.QuestionImage;
import com.eceplatform.QAForum.model.User;
import com.eceplatform.QAForum.repository.QuestionRepository;
import com.eceplatform.QAForum.repository.UserRepository;
import com.eceplatform.QAForum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addQuestion(QuestionRequest questionRequest) {

        User user = userRepository.findById(1).orElse(null);

        if(user != null){
            Question question = new Question();
            question.setContent(questionRequest.getContent());
            question.setUser(user);
            if(!CollectionUtils.isEmpty(questionRequest.getS3ImageKeys())){
                question.setImages(questionRequest.getS3ImageKeys().stream().map(key -> {
                    QuestionImage questionImage = new QuestionImage();
                    questionImage.setQuestion(question);
                    questionImage.setS3Key(key);
                    return questionImage;
                }).collect(Collectors.toList()));
            }
            questionRepository.save(question);
        } else {
            throw new RuntimeException("Invalid user");
        }

    }
}