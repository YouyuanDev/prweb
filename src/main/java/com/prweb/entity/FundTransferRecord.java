package com.prweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class FundTransferRecord {
    private  int id; //流水号
    private String transfer_no;
    private String company_no;
    private String fund_transfer_method;     //
    private float transfer_amount;   //
    private String payee_account;  //
    private String payee_real_name;
    private String transfer_status;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date transfer_date;
    private String sub_msg;
    private String alipay_out_biz_no;
    private String alipay_fund_order_id;

    public FundTransferRecord() {
    }

    public FundTransferRecord(int id, String transfer_no, String company_no, String fund_transfer_method, float transfer_amount, String payee_account, String payee_real_name, String transfer_status, Date transfer_date, String sub_msg, String alipay_out_biz_no, String alipay_fund_order_id) {
        this.id = id;
        this.transfer_no = transfer_no;
        this.company_no = company_no;
        this.fund_transfer_method = fund_transfer_method;
        this.transfer_amount = transfer_amount;
        this.payee_account = payee_account;
        this.payee_real_name = payee_real_name;
        this.transfer_status = transfer_status;
        this.transfer_date = transfer_date;
        this.sub_msg = sub_msg;
        this.alipay_out_biz_no = alipay_out_biz_no;
        this.alipay_fund_order_id = alipay_fund_order_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFund_transfer_method() {
        return fund_transfer_method;
    }

    public void setFund_transfer_method(String fund_transfer_method) {
        this.fund_transfer_method = fund_transfer_method;
    }

    public float getTransfer_amount() {
        return transfer_amount;
    }

    public void setTransfer_amount(float transfer_amount) {
        this.transfer_amount = transfer_amount;
    }

    public String getPayee_account() {
        return payee_account;
    }

    public void setPayee_account(String payee_account) {
        this.payee_account = payee_account;
    }

    public String getPayee_real_name() {
        return payee_real_name;
    }

    public void setPayee_real_name(String payee_real_name) {
        this.payee_real_name = payee_real_name;
    }

    public String getTransfer_status() {
        return transfer_status;
    }

    public void setTransfer_status(String transfer_status) {
        this.transfer_status = transfer_status;
    }

    public Date getTransfer_date() {
        return transfer_date;
    }

    public void setTransfer_date(Date transfer_date) {
        this.transfer_date = transfer_date;
    }

    public String getSub_msg() {
        return sub_msg;
    }

    public void setSub_msg(String sub_msg) {
        this.sub_msg = sub_msg;
    }

    public String getTransfer_no() {
        return transfer_no;
    }

    public void setTransfer_no(String transfer_no) {
        this.transfer_no = transfer_no;
    }

    public String getCompany_no() {
        return company_no;
    }

    public void setCompany_no(String company_no) {
        this.company_no = company_no;
    }

    public String getAlipay_out_biz_no() {
        return alipay_out_biz_no;
    }

    public void setAlipay_out_biz_no(String alipay_out_biz_no) {
        this.alipay_out_biz_no = alipay_out_biz_no;
    }

    public String getAlipay_fund_order_id() {
        return alipay_fund_order_id;
    }

    public void setAlipay_fund_order_id(String alipay_fund_order_id) {
        this.alipay_fund_order_id = alipay_fund_order_id;
    }
}
