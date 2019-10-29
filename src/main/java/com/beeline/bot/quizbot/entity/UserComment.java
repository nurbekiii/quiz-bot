package com.beeline.bot.quizbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * @author NIsaev on 11.10.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"created_at", "updated_at"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserComment implements Serializable {
    private Integer id;
    private String email;
    private String owner;
    private String comment3;

    public UserComment() {

    }

    public UserComment(Integer id, String email, String owner, String comment) {
        this.id = id;
        this.email = email;
        this.owner = owner;
        this.comment3 = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getComment3() {
        return comment3;
    }

    public void setComment3(String comment) {
        this.comment3 = comment;
    }
}
