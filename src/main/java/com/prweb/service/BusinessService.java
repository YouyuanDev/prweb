package com.prweb.service;

import com.prweb.entity.Business;

public interface BusinessService {



    //分页搜索
    public String getBusinessByLike(String business_no,String business_name,String business_type,int start,int rows);


    //根据business_no获取Business
    public String getBusinessByBusinessNo(String business_no);

    //获取所有business用于下拉框
    public String getAllBusiness();

    //保存Business
    public String saveBusiness(Business business);

    //删除Business信息
    public String delBusiness(String hlparam);

}
