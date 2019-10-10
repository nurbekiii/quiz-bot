package com.beeline.bot.quizbot.service.impl;

import com.beeline.bot.quizbot.entity.Task;
import com.beeline.bot.quizbot.service.TaskService;
import com.beeline.bot.quizbot.service.TaskService;
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
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${rest.url_main}")
    private String urlMain;

    @Value("${rest.jwt_token}")
    private String jwtToken;

    private String customUrl = "tasks/";

    @Override
    public  Task save(Task task){
        if (task.getId() == null)
            return create(task);

        return update(task);
    }

    @Override
    public Task create(Task task){
        try {
            HttpEntity<Task> requestEntity = new HttpEntity<>(task, getHttpHeaders());
            HttpEntity<Task> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.POST, requestEntity, Task.class);
            return response.getBody();
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public Task update(Task task) {
        try {
            long id = task.getId();
            HttpEntity<Task> requestEntity = new HttpEntity<>(task, getHttpHeaders());
            HttpEntity<Task> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.PUT, requestEntity, Task.class);
            return response.getBody();

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public Task getUserById(long id) {
        try {
            HttpEntity<Task> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.GET, null, Task.class);
            return response.getBody();

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public List<Task> getAll() {
        try {
            HttpEntity<Task> entity = new HttpEntity<>(null, getHttpHeaders());
            ResponseEntity<List<Task>> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Task>>() {
            });

            List<Task> list = response.getBody();
            return list;
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", "Bearer " + jwtToken);
        return headers;
    }
}
