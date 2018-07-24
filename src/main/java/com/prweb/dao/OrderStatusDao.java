package com.prweb.dao;

import com.prweb.entity.OrderStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderStatusDao {

    //根据status_code得到OrderStatus
    public List<OrderStatus> getOrderStatusByCode(@Param("status_code")String status_code);

    //得到所有OrderStatus
    public List<OrderStatus> getAllOrderStatus();
}
