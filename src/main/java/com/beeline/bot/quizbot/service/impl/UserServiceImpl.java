package com.beeline.bot.quizbot.service.impl;

import com.beeline.bot.quizbot.entity.User;
import com.beeline.bot.quizbot.service.UserService;
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
 * @author NIsaev on 26.09.2019
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeadersUtil httpHeadersUtil;

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
            User user2 = user;
            user2.setTempAttrNull(null);
            user2.setTasks(null);

            HttpEntity<User> requestEntity = new HttpEntity<>(user2, httpHeadersUtil.getHttpHeadersJson());
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
            HttpEntity<User> requestEntity = new HttpEntity<>(user, httpHeadersUtil.getHttpHeadersJson());
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
            HttpEntity<User> entity = new HttpEntity<>(null, httpHeadersUtil.getHttpHeadersJson());
            ResponseEntity<List<User>> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.GET, entity, new ParameterizedTypeReference<List<User>>() {
            });

            List<User> list = response.getBody();
            if (!list.isEmpty())
                return list.get(0);

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public User getUserByTlgId(long tlgId) {
        try {
            HttpEntity<User> entity = new HttpEntity<>(null, httpHeadersUtil.getHttpHeadersJson());
            ResponseEntity<List<User>> response = restTemplate.exchange(urlMain + customUrl + "/?tlg_id=" + tlgId, HttpMethod.GET, entity, new ParameterizedTypeReference<List<User>>() {
            });

            List<User> list = response.getBody();
            if (!list.isEmpty())
                return list.get(0);

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
}
