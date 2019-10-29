package com.beeline.bot.quizbot.service.impl;

import com.beeline.bot.quizbot.service.RemoteCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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

    @Autowired
    private RestTemplate restTemplate;

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
        ResponseEntity<List<T>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<T>>() {
        });

        List<T> list = response.getBody();
        /*ObjectMapper mapper = new ObjectMapper();

        List<Resp> list2 = mapper.convertValue(list, new TypeReference<Resp>() { });

        List<Resp> list3 = mapper.convertValue(list, new TypeReference<List<Resp>>() { });

        List<Resp> myObjects = mapper.convertValue(list, mapper.getTypeFactory().constructCollectionType(ArrayList.class, class1));*/

        return list;
    }

    @Override
    public T getSimpleObject(String url, Class<T> class1) throws Exception {
        /*ResponseEntity<T> response = restTemplate.exchange(new URI(url), HttpMethod.GET, null, class1);
        Resp result = response.getBody();
        return result;*/

        return null;
    }


}
