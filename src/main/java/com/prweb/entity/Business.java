package com.prweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Business {

    private  int id; //流水号
    private String business_no;
    private String business_name;
    private String business_type;
    private String valid;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date create_time;

    public Business() {
    }

    public Business(int id, String business_no, String business_name, String business_type, String valid, Date create_time) {
        this.id = id;
        this.business_no = business_no;
        this.business_name = business_name;
        this.business_type = business_type;
        this.valid = valid;
        this.create_time = create_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusiness_no() {
        return business_no;
    }

    public void setBusiness_no(String business_no) {
        this.business_no = business_no;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }
}
