package com.prweb.dao;

import com.prweb.entity.Function;
import com.prweb.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface RoleDao {

    public List<HashMap<String,Object>>getAllRoleByLike(@Param("role_no")String role_no, @Param("role_name")String role_name);
    //模糊搜索带分页
    public List<HashMap<String,Object>> getAllByLike(@Param("role_no")String role_no, @Param("role_name")String role_name, @Param("skip")int skip, @Param("take")int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("role_no")String role_no, @Param("role_name")String role_name);

    //修改role
    public int updateRole(Role role);
    //增加role
    public int addRole(Role role);
    //删除role
    public int delRole(String[]arrId);

    //根据Role No 得到Role
    public List<Role> getRoleByRoleNo(@Param("role_no")String role_no);

    //根据事件返回所有Role
    public List<HashMap<String,Object>> getRolesByEvent(@Param("event")String event);



}
