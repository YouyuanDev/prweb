package com.prweb.dao;

import com.prweb.entity.CompanyUser;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CompanyUserDao {

    //模糊搜索带分页
    public List<HashMap<String,Object>> getAllByLike(@Param("company_user_no")String company_user_no, @Param("company_name")String company_name, @Param("skip")int skip, @Param("take")int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("company_user_no")String company_user_no, @Param("company_name")String company_name);

    //根据company_user_no获得CompanyUser信息
    public List<CompanyUser> getCompanyUserByCompanyUserNo(@Param("company_user_no")String company_user_no);

    public List<CompanyUser>  getAllCompanyUser();


    //得到附近的商户
    public List<HashMap<String,Object>>  getNearByCompanyUser(@Param("center_coordinate_lon")String center_coordinate_lon,@Param("center_coordinate_lat")String center_coordinate_lat);


    //修改 CompanyUser
    public int updateCompanyUser(CompanyUser companyUser);
    //增加 CompanyUser
    public int addCompanyUser(CompanyUser companyUser);
    //删除 CompanyUser
    public int delCompanyUser(String[]arrId);
}
