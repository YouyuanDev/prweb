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
    private String company_user_no;
    private String person_user_location;
    private String company_user_location;
    private String service_items;
    private float service_fee;
    private String order_status;
    private String upload_files;
    private String remark;
    private String service_type_code;
    private String failure_type_code_list;
    private String payment_status;
    private String pay_method;
    private String trade_no;



    public Order() {
    }

    public Order(int id, String order_no, String business_no, Date order_time, Date finsh_time, String person_user_no, String company_user_no, String person_user_location, String company_user_location, String service_items, float service_fee, String order_status, String upload_files, String remark, String service_type_code, String failure_type_code_list, String payment_status, String pay_method, String trade_no) {
        this.id = id;
        this.order_no = order_no;
        this.business_no = business_no;
        this.order_time = order_time;
        this.finsh_time = finsh_time;
        this.person_user_no = person_user_no;
        this.company_user_no = company_user_no;
        this.person_user_location = person_user_location;
        this.company_user_location = company_user_location;
        this.service_items = service_items;
        this.service_fee = service_fee;
        this.order_status = order_status;
        this.upload_files = upload_files;
        this.remark = remark;
        this.service_type_code = service_type_code;
        this.failure_type_code_list = failure_type_code_list;
        this.payment_status = payment_status;
        this.pay_method = pay_method;
        this.trade_no = trade_no;
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

    public String getCompany_user_no() {
        return company_user_no;
    }

    public void setCompany_user_no(String company_user_no) {
        this.company_user_no = company_user_no;
    }

    public String getPerson_user_location() {
        return person_user_location;
    }

    public void setPerson_user_location(String person_user_location) {
        this.person_user_location = person_user_location;
    }

    public String getCompany_user_location() {
        return company_user_location;
    }

    public void setCompany_user_location(String company_user_location) {
        this.company_user_location = company_user_location;
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

    public String getUpload_files() {
        return upload_files;
    }

    public void setUpload_files(String upload_files) {
        this.upload_files = upload_files;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getService_type_code() {
        return service_type_code;
    }

    public void setService_type_code(String service_type_code) {
        this.service_type_code = service_type_code;
    }

    public String getFailure_type_code_list() {
        return failure_type_code_list;
    }

    public void setFailure_type_code_list(String failure_type_code_list) {
        this.failure_type_code_list = failure_type_code_list;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }


}
