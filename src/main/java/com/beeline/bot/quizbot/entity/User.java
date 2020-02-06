package com.beeline.bot.quizbot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author NIsaev on 26.09.2019
 */

@JsonIgnoreProperties(ignoreUnknown = true, value = {"role", "created_at", "updated_at", "provider", "blocked", "dob", "confirmed", "temp_attr", "tasks"})
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class User implements Serializable {
    private Integer id;
    private String username;
    private String email;
    private Integer points;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "tlg_id")
    private Long tlgId;

    @JsonProperty(value = "tlg_username")
    private String tlgUsername;

    @JsonProperty(value = "tlg_firstname")
    private String tlgFirstname;

    @JsonProperty(value = "tlg_lastname")
    private String tlgLastname;

    @JsonProperty(value = "tlg_fullname")
    private String tlgFullname;

    @JsonProperty(value = "temp_attr")
    @JsonIgnore
    private Map<String, Object> tempAttr;

    @JsonProperty(value = "tasks")
    @JsonIgnore
    private List<Task> tasks;

    public User() {
        tempAttr = new HashMap<>();
        tasks = new ArrayList<>();
    }

    public User(Integer id, String username, String email, Integer points, Long tlgId, String tlgUsername, String tlgFirstname, String tlgLastname, String tlgFullname) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.points = points;
        this.tlgId = tlgId;
        this.tlgUsername = tlgUsername;
        this.tlgFirstname = tlgFirstname;
        this.tlgLastname = tlgLastname;
        this.tlgFullname = tlgFullname;

        tempAttr = new HashMap<>();
        tasks = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Long getTlgId() {
        return tlgId;
    }

    public void setTlgId(Long tlgId) {
        this.tlgId = tlgId;
    }

    public String getTlgUsername() {
        return tlgUsername;
    }

    public void setTlgUsername(String tlgUsername) {
        this.tlgUsername = tlgUsername;
    }

    public String getTlgFirstname() {
        return tlgFirstname;
    }

    public void setTlgFirstname(String tlgFirstname) {
        this.tlgFirstname = tlgFirstname;
    }

    public String getTlgLastname() {
        return tlgLastname;
    }

    public void setTlgLastname(String tlgLastname) {
        this.tlgLastname = tlgLastname;
    }

    public String getTlgFullname() {
        return tlgFullname;
    }

    public void setTlgFullname(String tlg_fullname) {
        this.tlgFullname = tlg_fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTempAttrNull() {
        this.tempAttr = null;
    }

    public void setTasksNull() {
        this.tasks = null;
    }

    @JsonIgnore
    public Map<String, Object> getTempAttr() {
        return tempAttr;
    }

    public void setTempAttr(Map<String, Object> tempAttr) {
        if (tempAttr != null)
            this.tempAttr = tempAttr;
    }

    public void setTempAttr(String key, Object val) {
        this.tempAttr.put(key, val);
    }

    @JsonIgnore
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        if(tasks != null)
            this.tasks = tasks;
        else
            this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        this.tasks.remove(task);

        this.tasks.add(task);
    }

    public Object getTempAttr(String key) {
        return this.tempAttr.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (tlgUsername != null ? !tlgUsername.equals(user.tlgUsername) : user.tlgUsername != null) return false;
        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tlgUsername != null ? tlgUsername.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", points=" + points +
                ", password='" + password + '\'' +
                ", tlgId=" + tlgId +
                ", tlgUsername='" + tlgUsername + '\'' +
                ", tlgFirstname='" + tlgFirstname + '\'' +
                ", tlgLastname='" + tlgLastname + '\'' +
                ", tlgFullname='" + tlgFullname + '\'' +
                '}';
    }
}
