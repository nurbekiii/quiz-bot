package com.beeline.bot.quizbot.service.impl;

import com.beeline.bot.quizbot.entity.Category;
import com.beeline.bot.quizbot.service.CategoryService;
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
 * @author NIsaev on 18.10.2019
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeadersUtil httpHeadersUtil;

    @Value("${rest.url_main}")
    private String urlMain;

    @Value("${rest.jwt_token}")
    private String jwtToken;

    private String customUrl = "categories/";

    @Override
    public Category save(Category category) {
        if (category.getId() == null)
            return create(category);

        return update(category);
    }

    @Override
    public Category create(Category category) {
        try {
            HttpEntity<Category> requestEntity = new HttpEntity<>(category, httpHeadersUtil.getHttpHeadersJson());
            HttpEntity<Category> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.POST, requestEntity, Category.class);
            return response.getBody();
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public Category update(Category category) {
        try {
            long id = category.getId();
            HttpEntity<Category> requestEntity = new HttpEntity<>(category, httpHeadersUtil.getHttpHeadersJson());
            HttpEntity<Category> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.PUT, requestEntity, Category.class);
            return response.getBody();

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public Category getCategoryById(long id) {
        try {
            HttpEntity<Category> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.GET, null, Category.class);
            return response.getBody();

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public List<Category> getAll() {
        try {
            HttpEntity<Category> entity = new HttpEntity<>(null, httpHeadersUtil.getHttpHeadersJson());
            ResponseEntity<List<Category>> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Category>>() {
            });

            List<Category> list = response.getBody();
            return list;
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }
}
