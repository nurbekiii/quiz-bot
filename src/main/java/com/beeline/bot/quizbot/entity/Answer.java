package com.beeline.bot.quizbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.File;
import java.io.Serializable;

/**
 * @author NIsaev on 03.10.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"created_at", "updated_at"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Answer implements Serializable {
    private Integer id;
    private String email;
    private String owner;
    private String comment;
    private Integer point;

    @JsonProperty(value = "text_answer")
    private String textAnswer;

    @JsonProperty(value = "task_category")
    private String taskCategory;

    @JsonProperty(value = "user_id")
    private Integer userId;

    @JsonProperty(value = "task_id")
    private Integer taskId;

    @JsonProperty(value = "task_level")
    private Integer taskLevel;

    @JsonProperty(value = "task_name")
    private String taskName;

    @JsonProperty(value = "tlg_file_id")
    private String tlgFileId;

    @JsonProperty(value = "file_answer")
    private JsonNode fileAnswer;

    private byte[] fileContent;
    private File answerFile;

    public Answer() {

    }

    public Answer(Integer id, String email, String owner, String comment, String textAnswer, String taskCategory, Integer userId, Integer taskId, Integer taskLevel, String taskName, String tlgFileId, Integer point, JsonNode fileAnswer) {
        this.id = id;
        this.email = email;
        this.owner = owner;
        this.comment = comment;
        this.textAnswer = textAnswer;
        this.taskCategory = taskCategory;
        this.userId = userId;
        this.taskId = taskId;
        this.taskLevel = taskLevel;
        this.taskName = taskName;
        this.tlgFileId = tlgFileId;
        this.point = point;
        this.fileAnswer = fileAnswer;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(Integer taskLevel) {
        this.taskLevel = taskLevel;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTlgFileId() {
        return tlgFileId;
    }

    public void setTlgFileId(String tlgFileId) {
        this.tlgFileId = tlgFileId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public JsonNode getFileAnswer() {
        return fileAnswer;
    }

    public void setFileAnswer(JsonNode fileAnswer) {
        this.fileAnswer = fileAnswer;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public File getAnswerFile() {
        return answerFile;
    }

    public void setAnswerFile(File answerFile) {
        this.answerFile = answerFile;
    }
}
