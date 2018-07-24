package com.prweb.dao;

import com.prweb.entity.Account;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface AccountDao {


    //模糊搜索带分页
    public List<HashMap<String,Object>> getAllByLike(@Param("username")String username, @Param("account_status")String account_status,@Param("account_type")String account_type, @Param("skip")int skip, @Param("take")int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("username")String username, @Param("account_status")String account_status,@Param("account_type")String account_type);


    //根据username获取账号信息
    public List<Account>  getAccountByUserName(@Param("username")String username);

    //登录验证
    public List<Account>  VerifyUserNamePassword(@Param("username")String username,@Param("password")String password);


    //手机号登录验证
    public List<Account>  VerifyCellphoneNoPassword(@Param("cell_phone")String cell_phone,@Param("password")String password);

    //手机忘记密码
    public Account getPasswordByCellPhoneNo(@Param("cell_phone")String cell_phone);

    //修改Account
    public int updateAccount(Account account);
    //增加Account
    public int addAccount(Account account);
    //删除Account
    public int delAccount(String[]arrId);

}
