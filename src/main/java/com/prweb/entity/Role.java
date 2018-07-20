package com.prweb.entity;

public class Role {

    private int id;
    private String role_no;
    private String role_name;
    private String function_no_list;
    private String push_event_list;

    public Role() {
    }

    public Role(int id, String role_no, String role_name, String function_no_list, String push_event_list) {
        this.id = id;
        this.role_no = role_no;
        this.role_name = role_name;
        this.function_no_list = function_no_list;
        this.push_event_list = push_event_list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole_no() {
        return role_no;
    }

    public void setRole_no(String role_no) {
        this.role_no = role_no;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getFunction_no_list() {
        return function_no_list;
    }

    public void setFunction_no_list(String function_no_list) {
        this.function_no_list = function_no_list;
    }

    public String getPush_event_list() {
        return push_event_list;
    }

    public void setPush_event_list(String push_event_list) {
        this.push_event_list = push_event_list;
    }
}
