package com.prweb.dao;

import com.prweb.entity.CompanyUser;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CompanyUserDao {

    //根据company_user_no获得CompanyUser信息
    public List<CompanyUser> getCompanyUserByCompanyUserNo(@Param("company_user_no")String company_user_no);

    public List<CompanyUser>  getAllCompanyUser();

    //修改 CompanyUser
    public int updateCompanyUser(CompanyUser companyUser);
    //增加 CompanyUser
    public int addCompanyUser(CompanyUser companyUser);
    //删除 CompanyUser
    public int delCompanyUser(String[]arrId);
}
