package com.prweb.service;

import com.prweb.entity.Order;

import java.util.Date;

public interface OrderService {

    //APP使用 获取所有历史订单
    public String getAllOrderList(String username,String accountType,String page, String rows );

    //APP使用 获取当前订单
    public String getCurrentOrder(String username,String accountType);

    //搜索
    public String getOrderByLike(String order_no, String order_status, Date beginTime, Date endTime, int start, int rows);

    //根据order_no获得order
    public String getOrderByOrderNo(String order_no);

    //根据order_no获得order相关信息map
    public String getOrderMapByOrderNo(String order_no);

    //保存Order
    public String saveOrder(Order order,String basePath);

    //删除Order信息
    public String delOrder(String hlparam);

    //用于下拉框
    public String getAllOrderStatus();

    //获取当前订单的personUser和CompanyUser的Location
    public String getPersonUserAndCompanyUserCurrentLocation(String username,String accountType);

    //更新个人location到历史location，轨迹保存
    public String updateLocation(String username,String accountType, String order_no,String my_location);

    //获取当前订单的支付宝支付信息
    public String getCurrentOrderAliPayInfo(String username,String accountType);

    //支付宝支付回调函数
    public String AliPayNotify(String content,String sign,String out_trade_no,String trade_status,String trade_no,String basePath);

}
