package com.beeline.bot.quizbot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author NIsaev on 27.09.2019
 */
@Configuration
public class RestTemplateClient {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}