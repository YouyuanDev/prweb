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
}
