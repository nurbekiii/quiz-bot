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
@JsonIgnoreProperties(ignoreUnknown = true, value = {"created_at", "updated_at", "sha256", "provider", "size", "public_id"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileEx implements Serializable {
    private Long id;
    private String name;

    private String ext;
    private String mime;
    private String url;

    @JsonIgnore
    private String sha256;

    @JsonIgnore
    private String provider;

    @JsonIgnore
    private String size;

    @JsonIgnore
    private String public_id;

    @JsonIgnore
    @JsonProperty(value = "created_at")
    private LocalDateTime createdAt;

    @JsonIgnore
    @JsonProperty(value = "updated_at")
    private LocalDateTime updatedAt;

    public FileEx() {

    }

    public FileEx(Long id, String name, String ext, String mime, String url, String sha256, String provider, String size, String public_id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.ext = ext;
        this.mime = mime;
        this.url = url;
        this.sha256 = sha256;
        this.provider = provider;
        this.size = size;
        this.public_id = public_id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPublic_id() {
        return public_id;
    }

    public void setPublic_id(String public_id) {
        this.public_id = public_id;
    }
}
