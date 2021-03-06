package com.prweb.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.prweb.entity.Account;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface LoginService {

    //得到权限的json
    public JSONObject getFunctionJson(String username);

    //APP手机登录验证
    //public String LoginWithCellPhoneNo(String cellphoneno,String password);

    //APP手机登录验证
    public Account LoginWithCellPhoneNo(String cellphoneno, String password);

    //后台登录验证
    public boolean commitLogin(String username,String password);

    //APP手机忘记密码
    public String ForgotPassword(String cellphoneno) throws ClientException;

    //APP检测手机号是否可以注册
    public String APPIsCellphoneNoValidForRegister(String cellphoneno);

    //APP手机账户切换用户类型   个人用户 商户用户切换
    public JSONObject APPSwitchAccoutType(String ToAccountType,String username,String accountType);

    //APP手机注册
    public String APPRegister(String cellphoneno,String password,String verifycode);

    //发送验证码
    public String SendVerCodeSMS(String cellphoneno) throws ClientException;

    //APP修改密码
    public String APPChangePassword(String cellphoneno,String old_password,String new_password);

    //APP修改登录手机号
    public String APPChangeCellphone(String old_cellphoneno,String new_cellphoneno,String verifycode);

}
