package com.prweb.dao;

import com.prweb.entity.ServiceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServiceTypeDao {

    public List<ServiceType> getAllServiceType();
}
