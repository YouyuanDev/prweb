package com.prweb.service;

import com.prweb.entity.Company;

public interface CompanyUserService {

    //用于商户用户获取附近待接的订单
    public String getNearByPendingOrders(String lon,String lat,int start, int rows);

    //商户用户取消订单服务
    public String CompanyUserCancelOrder(String username,String accountType,String basePath);

    //商户用户完成订单服务
    public String CompanyUserFinishService(String username,String accountType,String basePath);

    //商户用户开始服务订单
    public String CompanyUserStartService(String username,String accountType,String basePath);

    //商户用户接订单
    public String CompanyUserAcceptPendingOrder(String username,String accountType,String order_no,String basePath);

    //获取CompanyUserInfo
    public String getCompanyUserInfo(String username);

    //保存CompanyUser
    public String saveCompanyUserInfo(Company company);

    //撤销认证CompanyUser
    public String cancelVerifyCompanyUserInfo(String username,String accountType);

    //认证CompanyUser
    public String verifyCompanyUserInfo(String username,String accountType,String id_card_picture_front,String id_card_picture_back,String business_certificate_picture);

    //获得company
    public Company getCompanyInfo(String username);

}
