package com.prweb.service;

import com.prweb.entity.ServiceType;

public interface ServiceTypeService {

    //得到所有的ServiceType
    public String getAllServiceType();

    //获取所有ServiceType列表
    public String getServiceTypeByLike(String service_type_name,int start,int rows);

    //保存ServiceType
    public String saveServiceType(ServiceType serviceType);

    //删除serviceType信息
    public String delServiceType(String hlparam );
}
