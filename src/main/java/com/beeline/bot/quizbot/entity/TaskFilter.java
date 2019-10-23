package com.beeline.bot.quizbot.entity;

/**
 * @author NIsaev on 21.10.2019
 */
public class TaskFilter {
    private Integer user_id;
    private Integer task_level;
    private String task_category;

    public TaskFilter(){

    }

    public TaskFilter(Integer user_id, Integer task_level, String task_category) {
        this.user_id = user_id;
        this.task_level = task_level;
        this.task_category = task_category;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getTask_level() {
        return task_level;
    }

    public void setTask_level(Integer task_level) {
        this.task_level = task_level;
    }

    public String getTask_category() {
        return task_category;
    }

    public void setTask_category(String task_category) {
        this.task_category = task_category;
    }
}
