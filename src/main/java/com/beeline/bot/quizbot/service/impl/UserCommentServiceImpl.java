package com.beeline.bot.quizbot.service.impl;

import com.beeline.bot.quizbot.entity.UserComment;
import com.beeline.bot.quizbot.service.UserCommentService;
import com.beeline.bot.quizbot.util.HttpHeadersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author NIsaev on 11.10.2019
 */
@Service
public class UserCommentServiceImpl implements UserCommentService {
    private static final Logger logger = LoggerFactory.getLogger(UserCommentServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeadersUtil httpHeadersUtil;

    @Value("${rest.url_main}")
    private String urlMain;

    @Value("${rest.jwt_token}")
    private String jwtToken;

    private String customUrl = "usercomments/";

    @Override
    public UserComment save(UserComment userComment) {
        if (userComment.getId() == null)
            return create(userComment);

        return update(userComment);
    }

    @Override
    public UserComment create(UserComment userComment) {
        try {
            HttpEntity<UserComment> requestEntity = new HttpEntity<>(userComment, httpHeadersUtil.getHttpHeadersJson());
            HttpEntity<UserComment> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.POST, requestEntity, UserComment.class);
            return response.getBody();
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public UserComment update(UserComment userComment) {
        try {
            long id = userComment.getId();
            HttpEntity<UserComment> requestEntity = new HttpEntity<>(userComment, httpHeadersUtil.getHttpHeadersJson());
            HttpEntity<UserComment> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.PUT, requestEntity, UserComment.class);
            return response.getBody();

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public UserComment getUserCommentById(long id) {
        try {
            HttpEntity<UserComment> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.GET, null, UserComment.class);
            return response.getBody();

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public List<UserComment> getAll() {
        try {
            HttpEntity<UserComment> entity = new HttpEntity<>(null, httpHeadersUtil.getHttpHeadersJson());
            ResponseEntity<List<UserComment>> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<List<UserComment>>() {
            });

            List<UserComment> list = response.getBody();
            return list;
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }


}
