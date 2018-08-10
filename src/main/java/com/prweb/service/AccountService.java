package com.prweb.service;

import com.prweb.entity.Account;

public interface AccountService {


    //分页搜索
    public String getAccountByLike(String username,String account_status,String account_type,int start,int rows);

    //保存Account
    public String saveAccount(Account account);

    //删除Account
    public String delAccount(String hlparam);

    //获取accountInfo
    public String getAccountInfo(String username);
}
