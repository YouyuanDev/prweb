package com.prweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CompanyUser {
    private  int id; //流水号
    private String company_user_no;
    private String company_no;

    public CompanyUser() {
    }

    public CompanyUser(int id, String company_user_no, String company_no) {
        this.id = id;
        this.company_user_no = company_user_no;
        this.company_no = company_no;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany_user_no() {
        return company_user_no;
    }

    public void setCompany_user_no(String company_user_no) {
        this.company_user_no = company_user_no;
    }

    public String getCompany_no() {
        return company_no;
    }

    public void setCompany_no(String company_no) {
        this.company_no = company_no;
    }
}
