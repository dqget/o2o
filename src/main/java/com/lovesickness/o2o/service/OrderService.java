package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.OrderExecution;
import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.entity.OrderProductMap;
import com.lovesickness.o2o.entity.PersonInfo;

import java.util.List;

public interface OrderService {
    /**
     * 添加订单
     *
     * @param order               订单信息
     * @param orderProductMapList 订单项
     * @return 操作结果
     */
    OrderExecution addOrder(Order order, List<OrderProductMap> orderProductMapList);

    /**
     * 根据订单Id查询订单信息
     *
     * @param user    用户信息
     * @param orderId 订单Id
     * @return 订单信息
     */
    Order getOrderById(PersonInfo user, Long orderId);

    /**
     * 根据订单No查询订单信息
     *
     * @param user         用户信息
     * @param orderNumbers 订单No
     * @return 订单信息
     */
    Order getOrderByNo(PersonInfo user, String orderNumbers);

    OrderExecution getOrderList();
}
