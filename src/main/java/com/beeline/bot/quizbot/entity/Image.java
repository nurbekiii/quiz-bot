package com.beeline.bot.quizbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author NIsaev on 01.10.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = { "created_at", "updated_at"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Image implements Serializable {

    private Long id;
    private String name;

    @JsonIgnore
    private String ext;

    @JsonIgnore
    private String mime;

    @JsonIgnore
    private String url;

    @JsonIgnore
    @JsonProperty(value = "created_at")
    private LocalDateTime createdAt;

    @JsonProperty(value = "updated_at")
    @JsonIgnore
    private LocalDateTime updatedAt;

    public Image(){

    }

    public Image(Long id, String name, String ext, String mime, String url) {
        this.id = id;
        this.name = name;
        this.ext = ext;
        this.mime = mime;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
