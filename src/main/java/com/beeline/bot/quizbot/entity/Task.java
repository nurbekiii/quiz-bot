package com.beeline.bot.quizbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * @author NIsaev on 02.10.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = { "created_at", "updated_at"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Task implements Serializable {
    private Integer id;
    private String title;
    private String description;
    private String code;
    private String result;
    private Integer order;

    public Task(){

    }

    public Task(Integer id, String title, String description, String code, String result, Integer order) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.code = code;
        this.result = result;
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
