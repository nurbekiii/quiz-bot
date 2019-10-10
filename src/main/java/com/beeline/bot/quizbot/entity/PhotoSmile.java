package com.beeline.bot.quizbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author NIsaev on 01.10.2019
 */

@JsonIgnoreProperties(ignoreUnknown = true, value = {"created_at", "updated_at"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PhotoSmile implements Serializable {

    private Long id;
    private String desc;
    private String username;
    private String owner;

    private byte [] imageContent;
    private File imageFile;

    @JsonIgnore
    @JsonProperty(value = "created_at")
    private LocalDateTime createdAt;

    @JsonProperty(value = "updated_at")
    @JsonIgnore
    private LocalDateTime updatedAt;

    private JsonNode image;

    public PhotoSmile() {
    }

    public PhotoSmile(Long id, String desc, String username, String owner, LocalDateTime createdAt, LocalDateTime updatedAt, JsonNode image) {
        this.id = id;
        this.desc = desc;
        this.username = username;
        this.owner = owner;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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


    public JsonNode getImage() {
        return image;
    }

    public void setImage(JsonNode image) {
        this.image = image;
    }

    public byte[] getImageContent() {
        return imageContent;
    }

    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
}
