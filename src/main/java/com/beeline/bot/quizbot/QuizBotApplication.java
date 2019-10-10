package com.beeline.bot.quizbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class QuizBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizBotApplication.class, args);
    }
}
