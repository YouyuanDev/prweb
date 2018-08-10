package com.prweb.service;

import com.prweb.entity.Role;

public interface RoleService {

    //获取所有role列表
    public String getRoleByLike(String role_no,String role_name,int start, int rows);

    //得到所有Roles
    public String getAllRoleByLike(String role_no,String role_name);

    //得到所有的推送事件
    public String getAllPushEventRule();

    //保存Role
    public String saveRole(Role role);

    //删除Role信息
    public String delRole(String hlparam);
}
