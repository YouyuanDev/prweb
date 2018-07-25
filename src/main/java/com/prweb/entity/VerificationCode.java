package com.prweb.entity;

import java.util.Date;

public class VerificationCode {

    private int id;
    private String cell_phone_no;
    private String verification_code;
    private Date expire_time;

    public VerificationCode() {
    }

    public VerificationCode(int id, String cell_phone_no, String verification_code, Date expire_time) {
        this.id = id;
        this.cell_phone_no = cell_phone_no;
        this.verification_code = verification_code;
        this.expire_time = expire_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCell_phone_no() {
        return cell_phone_no;
    }

    public void setCell_phone_no(String cell_phone_no) {
        this.cell_phone_no = cell_phone_no;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public Date getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(Date expire_time) {
        this.expire_time = expire_time;
    }
}
