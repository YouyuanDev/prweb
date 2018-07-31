package com.prweb.dao;

import com.prweb.entity.PersonUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PersonUserDao {

    //根据Account 的username 获得PersonUser信息
    public List<PersonUser> getPersonUserByUsername(@Param("username")String username);

    //根据person_user_no获得PersonUser信息
    public List<PersonUser> getPersonUserByPersonUserNo(@Param("person_user_no")String person_user_no);

    public List<PersonUser>  getAllPersonUser();

    //修改 PersonUser
    public int updatePersonUser(PersonUser personUser);
    //增加 PersonUser
    public int addPersonUser(PersonUser personUser);
    //删除 PersonUser
    public int delPersonUser(String[]arrId);
}
