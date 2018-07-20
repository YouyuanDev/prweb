package com.prweb.entity;

public class PushEventRule {
    private int id;
    private String push_event;
    private String push_event_name;


    public PushEventRule() {
    }

    public PushEventRule(int id, String push_event, String push_event_name) {
        this.id = id;
        this.push_event = push_event;
        this.push_event_name = push_event_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPush_event() {
        return push_event;
    }

    public void setPush_event(String push_event) {
        this.push_event = push_event;
    }

    public String getPush_event_name() {
        return push_event_name;
    }

    public void setPush_event_name(String push_event_name) {
        this.push_event_name = push_event_name;
    }


}
