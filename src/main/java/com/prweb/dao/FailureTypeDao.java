package com.prweb.dao;

import com.prweb.entity.FailureType;
import com.prweb.entity.ServiceType;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface FailureTypeDao {

    public List<FailureType> getAllFailureType();

    public List<HashMap<String,Object>> getAllByLike(@Param("failure_type_name")String failure_type_name, @Param("skip")int skip, @Param("take")int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("failure_type_name")String failure_type_name);

    //FailureType
    public int updateFailureType(FailureType failureType);
    //FailureType
    public int addFailureType(FailureType failureType);
    //删除FailureType
    public int delFailureType(String[]arrId);
}
