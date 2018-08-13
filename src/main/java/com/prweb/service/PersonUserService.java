package com.prweb.service;


import com.prweb.entity.Order;
import com.prweb.entity.PersonUser;


public interface PersonUserService {
    //个人用户确认订单完成，商户账户获得服务费
    public String confirmOrderFinish(String username,String accountType);

    //个人用户取消个人信息验证
    public String  cancelVerifyPersonUserInfo(String username, String accountType);

    //个人用户认证PersonUser个人信息
    public String verifyPersonUserInfo(String username, String accountType,String id_card_picture_back, String id_card_picture_front);

    //保存个人用户信息
    public String savePersonUserInfo(PersonUser personUser);

    //个人用户生成订单
    public String PersonUserSubmitPendingOrder(String username,String accountType,Order order,String basePath);

    //得到个人用户信息
    public String getPersonUserInfo(String username);

    //个人用户订单取消
    public String PersonUserCancelOrder(String username,String accountType,String basePath);

    //用于获取个人用户附近商户
    public String getNearByCompany(String lon,String lat,int start, int rows);


}
