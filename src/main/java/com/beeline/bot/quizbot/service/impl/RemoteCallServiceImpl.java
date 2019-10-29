package com.beeline.bot.quizbot.service.impl;

import com.beeline.bot.quizbot.service.RemoteCallService;
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
public class RemoteCallServiceImpl<T> implements RemoteCallService<T> {

    private static final Logger logger = LoggerFactory.getLogger(RemoteCallServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeadersUtil httpHeadersUtil;

    @Value("${rest.url_main}")
    private String urlMain;

    @Value("${rest.jwt_token}")
    private String jwtToken;

    @Override
    public T create(String url, T ent, Class<T> class1) {
        try {
            HttpEntity<T> requestEntity = new HttpEntity<>(ent, httpHeadersUtil.getHttpHeadersJson());
            HttpEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, class1);
            return response.getBody();
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public T sendPostRequest(String url, Object req, Class<T> class1) throws Exception {
        /*HttpEntity<T> request = new HttpEntity<>(req, getHttpHeaders());
        ResponseEntity<T> response = restTemplate.postForEntity(url, request, class1);
        HttpStatus httpCode = response.getStatusCode();
        if (!httpCode.is2xxSuccessful()) {
            throw new Exception(httpCode.toString());
        }

        return response.getBody();*/
        return null;
    }

    @Override
    public T sendPutRequest(String url, Object req, Class<T> class1) throws Exception {
        //HttpEntity<T> request = new HttpEntity<T>(req, getHttpHeaders());
        //restTemplate.put(url, request);

        return null;
    }

    @Override
    public T sendGetRequest(String url, Object req, Class<T> class1) throws Exception {
        /*HttpEntity<T> request = new HttpEntity<>(req, getHttpHeaders());
        ResponseEntity<T> response = restTemplate.getForEntity(url, class1, request);
        HttpStatus httpCode = response.getStatusCode();
        if (!httpCode.is2xxSuccessful()) {
            throw new Exception(httpCode.toString());
        }

        return response.getBody();*/
        return null;
    }

    @Override
    public List<T> getAll(String url, Class<T> class1) throws Exception {
        try {
            HttpEntity<T> entity = new HttpEntity<>(null, httpHeadersUtil.getHttpHeadersJson());
            ResponseEntity<List<T>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<T>>() {
            });
            List<T> list = response.getBody();

            return list;
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public T getSimpleObject(String url, Class<T> class1) throws Exception {
        /*ResponseEntity<T> response = restTemplate.exchange(new URI(url), HttpMethod.GET, null, class1);
        Resp result = response.getBody();
        return result;*/

        return null;
    }


}
