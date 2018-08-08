package com.prweb.service;

public interface PersonUserService {
    //个人用户确认订单完成，商户账户获得服务费
    public String confirmOrderFinish(String username,String accountType);

    //个人用户取消个人信息验证
    public String  cancelVerifyPersonUserInfo(String username, String accountType);

    //个人用户认证PersonUser个人信息
    public String verifyPersonUserInfo(String username, String accountType,String id_card_picture_back, String id_card_picture_front);
}
