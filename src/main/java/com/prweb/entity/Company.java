package com.prweb.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


public class Company {

    private  int id; //流水号
    private String company_no;
    private String company_name;
    private String company_tax_code;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date company_found_date;
    private String is_verified;
    private String contact_person;
    private String cell_phone;
    private String address;
    private String lane_line;
    private String company_location_lon;
    private String company_location_lat;
    private String id_card_picture_front;
    private String id_card_picture_back;
    private String business_certificate_picture;
    private String alipay_payee_account;
    private String alipay_payee_real_name;

    private float pr_account_amount;

    private String upload_files;



    public Company() {
    }

    public Company(int id, String company_no, String company_name, String company_tax_code, Date company_found_date, String is_verified, String contact_person, String cell_phone, String address, String lane_line, String company_location_lon, String company_location_lat, String id_card_picture_front, String id_card_picture_back, String business_certificate_picture, String alipay_payee_account, String alipay_payee_real_name, float pr_account_amount, String upload_files) {
        this.id = id;
        this.company_no = company_no;
        this.company_name = company_name;
        this.company_tax_code = company_tax_code;
        this.company_found_date = company_found_date;
        this.is_verified = is_verified;
        this.contact_person = contact_person;
        this.cell_phone = cell_phone;
        this.address = address;
        this.lane_line = lane_line;
        this.company_location_lon = company_location_lon;
        this.company_location_lat = company_location_lat;
        this.id_card_picture_front = id_card_picture_front;
        this.id_card_picture_back = id_card_picture_back;
        this.business_certificate_picture = business_certificate_picture;
        this.alipay_payee_account = alipay_payee_account;
        this.alipay_payee_real_name = alipay_payee_real_name;
        this.pr_account_amount = pr_account_amount;
        this.upload_files = upload_files;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany_no() {
        return company_no;
    }

    public void setCompany_no(String company_no) {
        this.company_no = company_no;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_tax_code() {
        return company_tax_code;
    }

    public void setCompany_tax_code(String company_tax_code) {
        this.company_tax_code = company_tax_code;
    }

    public Date getCompany_found_date() {
        return company_found_date;
    }

    public void setCompany_found_date(Date company_found_date) {
        this.company_found_date = company_found_date;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getCell_phone() {
        return cell_phone;
    }

    public void setCell_phone(String cell_phone) {
        this.cell_phone = cell_phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLane_line() {
        return lane_line;
    }

    public void setLane_line(String lane_line) {
        this.lane_line = lane_line;
    }

    public String getCompany_location_lon() {
        return company_location_lon;
    }

    public void setCompany_location_lon(String company_location_lon) {
        this.company_location_lon = company_location_lon;
    }

    public String getCompany_location_lat() {
        return company_location_lat;
    }

    public void setCompany_location_lat(String company_location_lat) {
        this.company_location_lat = company_location_lat;
    }

    public String getId_card_picture_front() {
        return id_card_picture_front;
    }

    public void setId_card_picture_front(String id_card_picture_front) {
        this.id_card_picture_front = id_card_picture_front;
    }

    public String getId_card_picture_back() {
        return id_card_picture_back;
    }

    public void setId_card_picture_back(String id_card_picture_back) {
        this.id_card_picture_back = id_card_picture_back;
    }

    public String getBusiness_certificate_picture() {
        return business_certificate_picture;
    }

    public void setBusiness_certificate_picture(String business_certificate_picture) {
        this.business_certificate_picture = business_certificate_picture;
    }

    public String getAlipay_payee_account() {
        return alipay_payee_account;
    }

    public void setAlipay_payee_account(String alipay_payee_account) {
        this.alipay_payee_account = alipay_payee_account;
    }

    public String getAlipay_payee_real_name() {
        return alipay_payee_real_name;
    }

    public void setAlipay_payee_real_name(String alipay_payee_real_name) {
        this.alipay_payee_real_name = alipay_payee_real_name;
    }

    public float getPr_account_amount() {
        return pr_account_amount;
    }

    public void setPr_account_amount(float pr_account_amount) {
        this.pr_account_amount = pr_account_amount;
    }

    public String getUpload_files() {
        return upload_files;
    }

    public void setUpload_files(String upload_files) {
        this.upload_files = upload_files;
    }
}
