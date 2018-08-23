package com.prweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Comment {

    private  int id; //流水号
    private String comment_no;
    private String order_no;
    private String rating;
    private String options;
    private String remark;
    private String comment_from_person_user_no;
    private String anonymous;
    private String comment_to_comany_no;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date comment_time;

    public Comment() {
    }

    public Comment(int id, String comment_no, String order_no, String rating, String options, String remark, String comment_from_person_user_no, String anonymous, String comment_to_comany_no, Date comment_time) {
        this.id = id;
        this.comment_no = comment_no;
        this.order_no = order_no;
        this.rating = rating;
        this.options = options;
        this.remark = remark;
        this.comment_from_person_user_no = comment_from_person_user_no;
        this.anonymous = anonymous;
        this.comment_to_comany_no = comment_to_comany_no;
        this.comment_time = comment_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment_no() {
        return comment_no;
    }

    public void setComment_no(String comment_no) {
        this.comment_no = comment_no;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComment_from_person_user_no() {
        return comment_from_person_user_no;
    }

    public void setComment_from_person_user_no(String comment_from_person_user_no) {
        this.comment_from_person_user_no = comment_from_person_user_no;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public String getComment_to_comany_no() {
        return comment_to_comany_no;
    }

    public void setComment_to_comany_no(String comment_to_comany_no) {
        this.comment_to_comany_no = comment_to_comany_no;
    }

    public Date getComment_time() {
        return comment_time;
    }

    public void setComment_time(Date comment_time) {
        this.comment_time = comment_time;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
}
