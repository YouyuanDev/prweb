package com.prweb.entity;

public class Function {
    private  int id; //流水号
    private String function_no;     //权限编号
    private String function_name;   //权限名称
    private String operation_type;  //操作类型
    private String uri;             //访问uri名称
    private String module_name;     //模块名称


    public Function() {
    }

    public Function(int id, String function_no, String function_name, String operation_type, String uri, String module_name) {
        this.id = id;
        this.function_no = function_no;
        this.function_name = function_name;
        this.operation_type = operation_type;
        this.uri = uri;
        this.module_name = module_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFunction_no() {
        return function_no;
    }

    public void setFunction_no(String function_no) {
        this.function_no = function_no;
    }

    public String getFunction_name() {
        return function_name;
    }

    public void setFunction_name(String function_name) {
        this.function_name = function_name;
    }

    public String getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(String operation_type) {
        this.operation_type = operation_type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }
}
