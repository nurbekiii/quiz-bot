package com.beeline.bot.quizbot.service.impl;

import com.beeline.bot.quizbot.entity.User;
import com.beeline.bot.quizbot.service.UserService;
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
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${rest.url_main}")
    private String urlMain;

    private String customUrl = "users/";

    @Override
    public User save(User user) {
        if (user.getId() == null)
            return create(user);

        return update(user);
    }

    @Override
    public User create(User user) {
        try {
            HttpEntity<User> requestEntity = new HttpEntity<>(user, getHttpHeaders());
            HttpEntity<User> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.POST, requestEntity, User.class);
            return response.getBody();
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public User update(User user) {
        try {
            int id = user.getId();
            HttpEntity<User> requestEntity = new HttpEntity<>(user, getHttpHeaders());
            HttpEntity<User> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.PUT, requestEntity, User.class);
            return response.getBody();

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public User getUserById(int id) {
        try {
            HttpEntity<User> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.GET, null, User.class);
            return response.getBody();

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        try {
            ResponseEntity<List<User>> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
            });

            List<User> list = response.getBody();
            return list;
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
