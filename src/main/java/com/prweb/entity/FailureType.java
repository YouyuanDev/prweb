package com.prweb.entity;

public class FailureType {

    private int id;
    private String failure_type_code;
    private String failure_type_name;
    private String failure_type_name_en;

    public FailureType() {
    }

    public FailureType(int id, String failure_type_code, String failure_type_name, String failure_type_name_en) {
        this.id = id;
        this.failure_type_code = failure_type_code;
        this.failure_type_name = failure_type_name;
        this.failure_type_name_en = failure_type_name_en;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFailure_type_code() {
        return failure_type_code;
    }

    public void setFailure_type_code(String failure_type_code) {
        this.failure_type_code = failure_type_code;
    }

    public String getFailure_type_name() {
        return failure_type_name;
    }

    public void setFailure_type_name(String failure_type_name) {
        this.failure_type_name = failure_type_name;
    }

    public String getFailure_type_name_en() {
        return failure_type_name_en;
    }

    public void setFailure_type_name_en(String failure_type_name_en) {
        this.failure_type_name_en = failure_type_name_en;
    }
}
