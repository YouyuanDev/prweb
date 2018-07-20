package com.prweb.dao;

import com.prweb.entity.Business;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface BusinessDao {

    //模糊搜索带分页
    public List<HashMap<String,Object>> getAllByLike(@Param("business_no")String business_no, @Param("business_name")String business_name, @Param("business_type")String business_type, @Param("skip")int skip, @Param("take")int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("business_no")String business_no, @Param("business_name")String business_name,@Param("business_type")String business_type);

    //根据business_no获得business信息
    public List<Business> getBusinessByBusinessNo(@Param("business_no")String business_no);

    public List<Business>  getAllBusiness();
    //修改 Business
    public int updateBusiness(Business business);
    //增加 Business
    public int addBusiness(Business business);
    //删除 Business
    public int delBusiness(String[]arrId);

}
