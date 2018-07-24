package com.prweb.dao;

import com.prweb.entity.ServiceType;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface ServiceTypeDao {

    public List<ServiceType> getAllServiceType();

    public List<HashMap<String,Object>> getAllByLike(@Param("service_type_name")String service_type_name, @Param("skip")int skip, @Param("take")int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("service_type_name")String service_type_name);

    //ServiceType
    public int updateServiceType(ServiceType serviceType);
    //ServiceType
    public int addServiceType(ServiceType serviceType);
    //删除ServiceType
    public int delServiceType(String[]arrId);
}
