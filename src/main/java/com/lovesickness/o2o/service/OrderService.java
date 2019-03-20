package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.OrderExecution;
import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.entity.OrderProductMap;

import java.util.List;

public interface OrderService {
    /**
     * 添加订单
     *
     * @param order                订单信息
     * @param OrderProductMapList 订单项
     * @return 操作结果
     */
    OrderExecution addOrder(Order order, List<OrderProductMap> OrderProductMapList);
}
