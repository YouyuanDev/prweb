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


    public Company() {
    }

    public Company(int id, String company_no, String company_name, String company_tax_code, Date company_found_date, String is_verified, String contact_person, String cell_phone, String address, String lane_line, String company_location_lon, String company_location_lat) {
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
    }
}
