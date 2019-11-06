package com.beeline.bot.quizbot.service.impl;

import com.beeline.bot.quizbot.entity.Answer;
import com.beeline.bot.quizbot.entity.TaskFilter;
import com.beeline.bot.quizbot.service.AnswerService;
import com.beeline.bot.quizbot.util.FileNameAwareByteArrayResource;
import com.beeline.bot.quizbot.util.HttpHeadersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author NIsaev on 03.10.2019
 */
@Service
public class AnswerServiceImpl implements AnswerService {
    private static final Logger logger = LoggerFactory.getLogger(AnswerServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeadersUtil httpHeadersUtil;

    @Value("${rest.url_main}")
    private String urlMain;

    @Value("${rest.jwt_token}")
    private String jwtToken;

    private String customUrl = "answers/";

    @Override
    public Answer save(Answer answer) {
        if (answer.getId() == null)
            return create(answer);

        return update(answer);
    }

    @Override
    public Answer create(Answer answer) {
        try {
            HttpEntity requestEntity = prepareForSend(answer);
            HttpEntity<Answer> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.POST, requestEntity, Answer.class);
            return response.getBody();
        } catch (Exception t) {
            t.printStackTrace();
            logger.error("ERROR: " + t.toString());
        }
        return null;
    }

    @Override
    public Answer update(Answer answer) {
        try {
            long id = answer.getId();
            HttpEntity requestEntity = prepareForSend(answer);
            HttpEntity<Answer> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.PUT, requestEntity, Answer.class);
            return response.getBody();

        } catch (Exception t) {
            t.printStackTrace();
            logger.error("ERROR: " + t.toString());
        }
        return null;
    }

    private HttpEntity prepareForSend(Answer answer) {
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Map<String, Object> form = new HashMap<>();
        if (answer.getId() != null)
            form.put("id", answer.getId());

        form.put("email", answer.getEmail());
        form.put("owner", answer.getOwner());
        form.put("comment", answer.getComment());
        form.put("point", answer.getPoint());
        form.put("task_category", answer.getTaskCategory());
        form.put("text_answer", answer.getTextAnswer());
        form.put("user_id", answer.getUserId());
        form.put("task_id", answer.getTaskId());
        form.put("task_level", answer.getTaskLevel());
        form.put("task_name", answer.getTaskName());
        form.put("tlg_file_id", answer.getTlgFileId());
        form.put("task_id", answer.getTaskId());
        form.put("file_orig_name", answer.getFileOrigName());

        String name = (answer.getAnswerFile() != null ? answer.getAnswerFile().getName() : null);

        MultiValueMap<String, Object> form3 = new LinkedMultiValueMap<>();
        form3.add("data", form);
        form3.add("files.file_answer", name == null ? null : new FileNameAwareByteArrayResource(name, answer.getFileContent(), null));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(form3, httpHeadersUtil.getHttpHeadersMultiPart());
        return requestEntity;
    }

    @Override
    public Answer getAnswerById(long id) {
        try {
            HttpEntity<Answer> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.GET, null, Answer.class);
            return response.getBody();

        } catch (Exception t) {
            t.printStackTrace();
            logger.error("ERROR: " + t.toString());
        }
        return null;
    }

    @Override
    public List<Answer> getAll() {
        try {
            HttpEntity<Answer> entity = new HttpEntity<>(null, httpHeadersUtil.getHttpHeadersJson());
            ResponseEntity<List<Answer>> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Answer>>() {
            });

            List<Answer> list = response.getBody();
            return list;
        } catch (Exception t) {
            t.printStackTrace();
            logger.error("ERROR: " + t.toString());
        }
        return null;
    }

    @Override
    public List<Answer> getAnswersByFilter(TaskFilter filter) {
        try {
            String params = formatCriteria(filter);

            HttpEntity<Answer> entity = new HttpEntity<>(null, httpHeadersUtil.getHttpHeadersJson());
            ResponseEntity<List<Answer>> response = restTemplate.exchange(urlMain + customUrl + "?" + params, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Answer>>() {
            });

            List<Answer> list = response.getBody();
            return list;
        } catch (Exception t) {
            t.printStackTrace();
            logger.error("ERROR: " + t.toString());
        }
        return null;
    }

    private String formatCriteria(TaskFilter filter) {
        if (filter.getTask_level() == null && filter.getTask_category() == null)
            return String.format("user_id=%s&_sort=id:DESC", filter.getUser_id());

        if (filter.getTask_level() == null)
            return String.format("user_id=%s&task_category=%s&_sort=id:DESC", filter.getUser_id(), filter.getTask_category());

        return String.format("user_id=%s&task_level=%s&task_category=%s&_sort=id:DESC", filter.getUser_id(), filter.getTask_level(), filter.getTask_category());
    }

}
