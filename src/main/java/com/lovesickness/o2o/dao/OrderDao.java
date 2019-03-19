package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Order;

public interface OrderDao {
    /**
     * 根据订单Id
     *
     * @param orderId 订单Id
     * @return 订单信息
     */
    Order queryOrderById(Long orderId);
    /**
     * 根据订单No
     *
     * @param orderNumber 订单No
     * @return 订单信息
     */
    Order queryOrderByNo(String orderNumber);

    /**
     * 添加订单
     *
     * @param order
     * @return
     */
    int insertOrder(Order order);
}
