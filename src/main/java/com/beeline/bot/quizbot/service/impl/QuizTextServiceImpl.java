package com.beeline.bot.quizbot.service.impl;

import com.beeline.bot.quizbot.entity.QuizText;
import com.beeline.bot.quizbot.entity.User;
import com.beeline.bot.quizbot.service.QuizTextService;
import com.beeline.bot.quizbot.service.UserService;
import com.beeline.bot.quizbot.util.HttpHeadersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author NIsaev on 26.09.2019
 */
@Service
public class QuizTextServiceImpl implements QuizTextService {
    private static final Logger logger = LoggerFactory.getLogger(QuizTextServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeadersUtil httpHeadersUtil;

    @Value("${rest.url_main}")
    private String urlMain;

    @Value("${rest.jwt_token}")
    private String jwtToken;

    private String customUrl = "quiztexts/";

    @Override
    public  QuizText save(QuizText quiz){
        if (quiz.getId() == null)
            return create(quiz);

        return update(quiz);
    }

    @Override
    public QuizText create(QuizText quiz){
        try {
            HttpEntity<QuizText> requestEntity = new HttpEntity<>(quiz, httpHeadersUtil.getHttpHeadersJson());
            HttpEntity<QuizText> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.POST, requestEntity, QuizText.class);
            return response.getBody();
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public QuizText update(QuizText quiz) {
        try {
            long id = quiz.getId();
            HttpEntity<QuizText> requestEntity = new HttpEntity<>(quiz, httpHeadersUtil.getHttpHeadersJson());
            HttpEntity<QuizText> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.PUT, requestEntity, QuizText.class);
            return response.getBody();

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public QuizText getUserById(long id) {
        try {
            HttpEntity<QuizText> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.GET, null, QuizText.class);
            return response.getBody();

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public List<QuizText> getAll() {
        try {
            HttpEntity<QuizText> entity = new HttpEntity<>(null, httpHeadersUtil.getHttpHeadersJson());
            ResponseEntity<List<QuizText>> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<List<QuizText>>() {
            });

            List<QuizText> list = response.getBody();
            return list;
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }
}
