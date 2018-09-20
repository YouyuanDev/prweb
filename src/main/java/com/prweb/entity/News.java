package com.prweb.entity;

import java.util.Date;

public class News {
    private int id;
    private String username;
    private String title;
    private String content;
    private Date publish_time;

    public News() {
    }

    public News(int id, String username, String title, String content, Date publish_time) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.content = content;
        this.publish_time = publish_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(Date publish_time) {
        this.publish_time = publish_time;
    }
}
