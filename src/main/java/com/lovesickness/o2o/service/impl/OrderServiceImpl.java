package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.OrderDao;
import com.lovesickness.o2o.dao.OrderProductMapDao;
import com.lovesickness.o2o.dto.OrderExecution;
import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.entity.OrderProductMap;
import com.lovesickness.o2o.enums.OrderStateEnum;
import com.lovesickness.o2o.exception.OrderOperationException;
import com.lovesickness.o2o.exception.OrderProductMapOperationException;
import com.lovesickness.o2o.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderProductMapDao orderProductMapDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public OrderExecution addOrder(Order order, List<OrderProductMap> orderProductMapList) {
        //order信息不能为空
        if (order != null && order.getUser() != null && order.getUser().getUserId() != null) {
            //orderProductMap订单项信息不能为空
            if (orderProductMapList != null && orderProductMapList.size() != 0) {
                order.setCreateTime(new Date());
                order.setUpdateTime(new Date());
                order.setStatus(1);
                try {
                    int effectedNum = orderDao.insertOrder(order);
                    if (effectedNum != 1) {
                        throw new OrderOperationException("创建订单失败");
                    }
                } catch (Exception e) {
                    throw new OrderOperationException("addOrder error" + e.getMessage());
                }
                //添加订单项
                addOrderProcuctMap(order.getOrderId(), orderProductMapList);
                return new OrderExecution(OrderStateEnum.SUCCESS, order);
            } else {
                return new OrderExecution(OrderStateEnum.EMPTY_MAP);
            }
        } else {
            return new OrderExecution(OrderStateEnum.EMPTY);
        }
    }

    /**
     * 添加订单项
     *
     * @param orderId             订单Id
     * @param orderProductMapList 订单项列表
     */
    private void addOrderProcuctMap(Long orderId, List<OrderProductMap> orderProductMapList) {
        int size = orderProductMapList.size();
        orderProductMapList.forEach(orderProductMap -> {
            orderProductMap.setOrderId(orderId);
            orderProductMap.setCreateTime(new Date());
            orderProductMap.setUpdateTime(new Date());
            orderProductMap.setStatus(1);
        });
        try {
            int effectedNum = orderProductMapDao.batchInsertOrderProductMap(orderProductMapList);
            if (effectedNum != size) {
                throw new OrderProductMapOperationException("创建订单项失败");
            }
        } catch (Exception e) {
            throw new OrderProductMapOperationException("addOrderProcuctMap error" + e.getMessage());
        }

    }
}
