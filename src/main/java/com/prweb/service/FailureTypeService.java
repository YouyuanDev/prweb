package com.prweb.service;


import com.prweb.entity.FailureType;

public interface FailureTypeService {



    //得到所有的FailureType
    public String getAllFailureType();

    //获取所有FailureType列表
    public String getFailureTypeByLike(String failure_type_name,int start, int rows);

    //保存FailureType
    public String saveFailureType(FailureType failureType);

    //删除failureType信息
    public String delFailureType(String hlparam);
}
