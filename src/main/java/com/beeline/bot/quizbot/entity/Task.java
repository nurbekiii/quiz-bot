package com.beeline.bot.quizbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;

/**
 * @author NIsaev on 02.10.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"created_at", "updated_at"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Task implements Serializable {
    private Integer id;
    private String title;
    private String description;
    private String code;
    private String result;
    private Integer order;

    @JsonProperty(value = "same_task_id")
    private Integer sameTaskId;

    @JsonProperty(value = "file_id")
    private String fileId;

    @JsonProperty(value = "file_type")
    private String fileType;


    @JsonProperty(value = "attachs")
    private List<FileEx> attachments;

    @JsonIgnore
    private boolean isFinished;

    public Task() {

    }

    public Task(Integer id, String title, String description, String code, String result, Integer order, Integer sameTaskId, String fileId, String fileType, List<FileEx> attachments, boolean isFinished) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.code = code;
        this.result = result;
        this.order = order;
        this.sameTaskId = sameTaskId;
        this.fileId = fileId;
        this.fileType = fileType;
        this.attachments = attachments;
        this.isFinished = isFinished;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public Integer getSameTaskId() {
        return sameTaskId;
    }

    public void setSameTaskId(Integer sameTaskId) {
        this.sameTaskId = sameTaskId;
    }

    public List<FileEx> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<FileEx> attachments) {
        this.attachments = attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!id.equals(task.id)) return false;
        if (title != null ? !title.equals(task.title) : task.title != null) return false;
        return code != null ? code.equals(task.code) : task.code == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }
}
