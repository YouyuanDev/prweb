package com.prweb.entity;

public class OrderStatus {
    private  int id; //流水号
    private String status_code;
    private String status_name;
    private String status_en;

    public OrderStatus() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getStatus_en() {
        return status_en;
    }

    public void setStatus_en(String status_en) {
        this.status_en = status_en;
    }
}
