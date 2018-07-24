package com.prweb.entity;

import java.util.Date;

public class Location {
    private  int id; //流水号
    private String username;     //登录用户名
    private String order_no;     //订单编号
    private String coordinate;     //坐标
    private Date locating_time;     //定位时间

    public Location() {
    }

    public Location(int id, String username, String order_no, String coordinate, Date locating_time) {
        this.id = id;
        this.username = username;
        this.order_no = order_no;
        this.coordinate = coordinate;
        this.locating_time = locating_time;
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

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public Date getLocating_time() {
        return locating_time;
    }

    public void setLocating_time(Date locating_time) {
        this.locating_time = locating_time;
    }
}
