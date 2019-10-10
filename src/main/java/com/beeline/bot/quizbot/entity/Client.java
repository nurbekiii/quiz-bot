package com.beeline.bot.quizbot.entity;

/**
 * @author NIsaev on 01.10.2019
 */
public class Client {
    private Long id;
    private String tlgUserName;

    private String tlgFirstName;
    private String tlgLastName;

    private String clntFullName;

    private Integer askNameMessId;
    private Integer askTask1MessId;

    private String task1FielId;

    private Integer points;

    public Client(Long id, String tlgUserName, String tlgFirstName, String tlgLastName) {
        this.id = id;
        this.tlgUserName = tlgUserName;
        this.tlgFirstName = tlgFirstName;
        this.tlgLastName = tlgLastName;
    }

    public Client(Long id, String tlgUserName, String tlgFirstName, String tlgLastName, String clntFullName) {
        this.id = id;
        this.tlgUserName = tlgUserName;
        this.tlgFirstName = tlgFirstName;
        this.tlgLastName = tlgLastName;
        this.clntFullName = clntFullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTlgFirstName() {
        return tlgFirstName;
    }

    public void setTlgFirstName(String tlgFirstName) {
        this.tlgFirstName = tlgFirstName;
    }

    public String getTlgLastName() {
        return tlgLastName;
    }

    public void setTlgLastName(String tlgLastName) {
        this.tlgLastName = tlgLastName;
    }

    public String getTlgUserName() {
        return tlgUserName;
    }

    public void setTlgUserName(String tlgUserName) {
        this.tlgUserName = tlgUserName;
    }

    public String getClntFullName() {
        return clntFullName;
    }

    public void setClntFullName(String clntFullName) {
        this.clntFullName = clntFullName;
    }

    public Integer getAskNameMessId() {
        return askNameMessId;
    }

    public void setAskNameMessId(Integer askNameMessId) {
        this.askNameMessId = askNameMessId;
    }

    public Integer getAskTask1MessId() {
        return askTask1MessId;
    }

    public void setAskTask1MessId(Integer askTask1MessId) {
        this.askTask1MessId = askTask1MessId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getTask1FielId() {
        return task1FielId;
    }

    public void setTask1FielId(String task1FielId) {
        this.task1FielId = task1FielId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (!id.equals(client.id)) return false;
        return tlgUserName.equals(client.tlgUserName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + tlgUserName.hashCode();
        return result;
    }
}

