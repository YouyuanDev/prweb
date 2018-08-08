package com.prweb.service;

import com.alibaba.fastjson.JSONObject;
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


}
