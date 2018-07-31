package com.prweb.dao;

import com.prweb.entity.Company;
import com.prweb.entity.CompanyUser;
import com.prweb.entity.PersonUser;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CompanyUserDao {

    //根据Account 的username 获得Company信息
    public List<Company> getCompanyInfoByUsername(@Param("username")String username);

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
