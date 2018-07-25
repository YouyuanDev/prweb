package com.prweb.dao;

import com.prweb.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface OrderDao {

    //模糊搜索带分页
    public List<HashMap<String,Object>> getAllByLike(@Param("order_no")String order_no, @Param("order_status")String order_status, @Param("begin_time")Date begin_time, @Param("end_time")Date end_time, @Param("skip")int skip, @Param("take")int take);

    //模糊搜索总数
    public int getCountAllByLike(@Param("order_no")String order_no, @Param("order_status")String order_status,@Param("begin_time")Date begin_time, @Param("end_time")Date end_time);

    //修改order
    public int updateOrder(Order order);
    //增加order
    public int addOrder(Order order);
    //删除order
    public int delOrder(String[]arrId);

    //根据order_no得到Order
    public List<Order> getOrderByOrderNo(@Param("order_no")String order_no);

    //获取person_user的当前正在进行中订单
    public Order getCurrentPersonUserOrderByUsername(@Param("person_username")String person_username);

    //获取company_user的当前正在进行中订单
    public Order getCurrentOrderCompanyUserByUsername(@Param("company_username")String company_username);
}
