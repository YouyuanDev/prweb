package com.prweb.entity;

public class ServiceType {

    private int id;
    private String service_type_code;
    private String service_type_name;
    private String service_type_name_en;


    public ServiceType() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getService_type_code() {
        return service_type_code;
    }

    public void setService_type_code(String service_type_code) {
        this.service_type_code = service_type_code;
    }

    public String getService_type_name() {
        return service_type_name;
    }

    public void setService_type_name(String service_type_name) {
        this.service_type_name = service_type_name;
    }

    public String getService_type_name_en() {
        return service_type_name_en;
    }

    public void setService_type_name_en(String service_type_name_en) {
        this.service_type_name_en = service_type_name_en;
    }
}
