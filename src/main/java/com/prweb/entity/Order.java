package com.prweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Order {
    private  int id; //流水号
    private String order_no;
    private String business_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date order_time;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date finsh_time;
    private String person_user_no;
    private String company_user_info;
    private String person_user_location;
    private String service_items;
    private float service_fee;
    private String order_status;


    public Order() {
    }


    public Order(int id, String order_no, String business_no, Date order_time, Date finsh_time, String person_user_no, String company_user_info, String person_user_location, String service_items, float service_fee, String order_status) {
        this.id = id;
        this.order_no = order_no;
        this.business_no = business_no;
        this.order_time = order_time;
        this.finsh_time = finsh_time;
        this.person_user_no = person_user_no;
        this.company_user_info = company_user_info;
        this.person_user_location = person_user_location;
        this.service_items = service_items;
        this.service_fee = service_fee;
        this.order_status = order_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getBusiness_no() {
        return business_no;
    }

    public void setBusiness_no(String business_no) {
        this.business_no = business_no;
    }

    public Date getOrder_time() {
        return order_time;
    }

    public void setOrder_time(Date order_time) {
        this.order_time = order_time;
    }

    public Date getFinsh_time() {
        return finsh_time;
    }

    public void setFinsh_time(Date finsh_time) {
        this.finsh_time = finsh_time;
    }

    public String getPerson_user_no() {
        return person_user_no;
    }

    public void setPerson_user_no(String person_user_no) {
        this.person_user_no = person_user_no;
    }

    public String getCompany_user_info() {
        return company_user_info;
    }

    public void setCompany_user_info(String company_user_info) {
        this.company_user_info = company_user_info;
    }

    public String getPerson_user_location() {
        return person_user_location;
    }

    public void setPerson_user_location(String person_user_location) {
        this.person_user_location = person_user_location;
    }

    public String getService_items() {
        return service_items;
    }

    public void setService_items(String service_items) {
        this.service_items = service_items;
    }

    public float getService_fee() {
        return service_fee;
    }

    public void setService_fee(float service_fee) {
        this.service_fee = service_fee;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
