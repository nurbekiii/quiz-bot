package com.beeline.bot.quizbot.service.impl;

import com.beeline.bot.quizbot.entity.PhotoSmile;
import com.beeline.bot.quizbot.service.PhotoSmileService;
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
 * @author NIsaev on 01.10.2019
 */

@Service
public class PhotoSmileServiceImpl implements PhotoSmileService {

    private static final Logger logger = LoggerFactory.getLogger(PhotoSmileServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpHeadersUtil httpHeadersUtil;

    @Value("${rest.url_main}")
    private String urlMain;

    @Value("${rest.jwt_token}")
    private String jwtToken;

    private String customUrl = "zphotosmiles/";

    @Override
    public PhotoSmile save(PhotoSmile smile) {
        if (smile.getId() == null)
            return create(smile);

        return update(smile);
    }

    @Override
    public PhotoSmile create(PhotoSmile smile) {
        try {
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Map<String, Object> form = new HashMap<>();
            form.put("username", smile.getUsername());
            form.put("owner", smile.getOwner());
            form.put("desc", smile.getDesc());

            String name = smile.getImageFile().getName();

            MultiValueMap<String, Object> form3 = new LinkedMultiValueMap<>();
            form3.add("data", form);
            form3.add("files.image", new FileNameAwareByteArrayResource(name, smile.getImageContent(), null));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(form3, httpHeadersUtil.getHttpHeadersMultiPart());

            HttpEntity<PhotoSmile> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.POST, requestEntity, PhotoSmile.class);
            return response.getBody();
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public PhotoSmile update(PhotoSmile smile) {
        try {
            long id = smile.getId();
            HttpEntity<PhotoSmile> requestEntity = new HttpEntity<>(smile, httpHeadersUtil.getHttpHeadersJson());
            HttpEntity<PhotoSmile> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.PUT, requestEntity, PhotoSmile.class);
            return response.getBody();

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public PhotoSmile getPhotoSmileById(int id) {
        try {
            HttpEntity<PhotoSmile> response = restTemplate.exchange(urlMain + customUrl + id, HttpMethod.GET, null, PhotoSmile.class);
            return response.getBody();

        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }

    @Override
    public List<PhotoSmile> getAllPhotoSmiles() {
        try {
            HttpEntity<PhotoSmile> entity = new HttpEntity<>(null, httpHeadersUtil.getHttpHeadersJson());
            ResponseEntity<List<PhotoSmile>> response = restTemplate.exchange(urlMain + customUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<List<PhotoSmile>>() {
            });

            List<PhotoSmile> list = response.getBody();
            return list;
        } catch (Exception t) {
            logger.error(t.toString());
        }
        return null;
    }


}
