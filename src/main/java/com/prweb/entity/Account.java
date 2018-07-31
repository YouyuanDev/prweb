package com.prweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Account {

    private  int id; //流水号
    private String username;
    private String password;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date register_time;
    private String cell_phone;
    private String role_no_list;
    private String account_type;
    private String person_user_no;
    private String company_user_no;
    private String system_user_no;
    private String account_status;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date last_login_time;
    private String icon_url;
    private String nickname;

    public Account() {
    }

    public Account(int id, String username, String password, Date register_time, String cell_phone, String role_no_list, String account_type, String person_user_no, String company_user_no, String system_user_no, String account_status, Date last_login_time, String icon_url, String nickname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.register_time = register_time;
        this.cell_phone = cell_phone;
        this.role_no_list = role_no_list;
        this.account_type = account_type;
        this.person_user_no = person_user_no;
        this.company_user_no = company_user_no;
        this.system_user_no = system_user_no;
        this.account_status = account_status;
        this.last_login_time = last_login_time;
        this.icon_url = icon_url;
        this.nickname = nickname;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegister_time() {
        return register_time;
    }

    public void setRegister_time(Date register_time) {
        this.register_time = register_time;
    }

    public String getCell_phone() {
        return cell_phone;
    }

    public void setCell_phone(String cell_phone) {
        this.cell_phone = cell_phone;
    }

    public String getRole_no_list() {
        return role_no_list;
    }

    public void setRole_no_list(String role_no_list) {
        this.role_no_list = role_no_list;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
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

    public String getSystem_user_no() {
        return system_user_no;
    }

    public void setSystem_user_no(String system_user_no) {
        this.system_user_no = system_user_no;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    public Date getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(Date last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
