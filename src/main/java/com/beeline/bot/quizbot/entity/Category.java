package com.beeline.bot.quizbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @author NIsaev on 18.10.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"created_at", "updated_at"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Category {
    private Long id;
    private String title;
    private String code;

    public Category() {
    }

    public Category(Long id, String title, String code) {
        this.id = id;
        this.title = title;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

