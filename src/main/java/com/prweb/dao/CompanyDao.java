package com.prweb.dao;

import com.prweb.entity.Company;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CompanyDao {

    //模糊搜索带分页
    public List<HashMap<String,Object>> getAllByLike(@Param("company_no")String company_no, @Param("company_name")String company_name, @Param("skip")int skip, @Param("take")int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("company_no")String company_no, @Param("company_name")String company_name);

    //根据company_no获得Company信息
    public List<Company> getCompanyByCompanyNo(@Param("company_no")String company_no);

    public List<Company>  getAllCompany();


    //得到附近的商户
    public List<HashMap<String,Object>>  getNearByCompany(@Param("center_coordinate_lon")String center_coordinate_lon,@Param("center_coordinate_lat")String center_coordinate_lat);


    //修改 Company
    public int updateCompany(Company company);
    //增加 Company
    public int addCompany(Company company);
    //删除 Company
    public int delCompany(String[]arrId);

}
