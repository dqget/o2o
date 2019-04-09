package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.OrderDao;
import com.lovesickness.o2o.dao.OrderProductMapDao;
import com.lovesickness.o2o.dto.OrderExecution;
import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.entity.OrderProductMap;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.enums.OrderStateEnum;
import com.lovesickness.o2o.exception.OrderOperationException;
import com.lovesickness.o2o.exception.OrderProductMapOperationException;
import com.lovesickness.o2o.service.OrderService;
import com.lovesickness.o2o.util.PageCalculator;
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

    @Override
    public Order getOrderById(PersonInfo user, Long orderId) {
        return orderDao.queryOrderById(orderId);
    }

    @Override
    public Order getOrderByNo(PersonInfo user, String orderNumbers) {
        return orderDao.queryOrderByNo(orderNumbers);
    }

    @Override
    public OrderExecution getOrderList(Order order, String keyWord, Integer pageIndex, Integer pageSize) {
        int bagenIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        OrderExecution oe = new OrderExecution();
        oe.setOrderList(orderDao.queryOrderList(order, keyWord, bagenIndex, pageSize));
        oe.setCount(orderDao.queryOrderCount(order, keyWord));
        return oe;
    }

    @Override
    public OrderExecution getOrderNotEvaList(Long userId, Long shopId, String keyWord, Integer pageIndex, Integer pageSize) {
        int bagenIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        OrderExecution oe = new OrderExecution();
        oe.setOrderList(orderDao.queryNotEvaOrderList(userId, shopId, keyWord, bagenIndex, pageSize));
        oe.setCount(orderDao.queryNotEvaOrderCount(userId, shopId, keyWord));
        return oe;
    }

    @Override
    @Transactional(rollbackFor = OrderOperationException.class)
    public OrderExecution modifyOrderByUser(Order order) throws OrderOperationException {
        Long userId;
        if (order.getOrderId() == null || order.getUser() == null || (userId = order.getUser().getUserId()) == null) {
            return new OrderExecution(OrderStateEnum.EMPTY);
        }
        Order oldOrder = orderDao.queryOrderById(order.getOrderId());
        if (!oldOrder.getUser().getUserId().equals(userId)) {
            return new OrderExecution(OrderStateEnum.INNER_ERROR);
        }
        int effectedNum = orderDao.updateOrder(order);
        if (effectedNum != 1) {
            throw new OrderOperationException("修改订单失败");
        }
        return new OrderExecution(OrderStateEnum.SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = OrderOperationException.class)
    public OrderExecution modifyOrderByShop(Order order) throws OrderOperationException {
        Long shopId;
        if (order.getOrderId() == null || order.getShop() == null || (shopId = order.getShop().getShopId()) == null) {
            return new OrderExecution(OrderStateEnum.EMPTY);
        }
        Order oldOrder = orderDao.queryOrderById(order.getOrderId());
        if (!oldOrder.getShop().getShopId().equals(shopId)) {
            return new OrderExecution(OrderStateEnum.INNER_ERROR);
        }
        int effectedNum = orderDao.updateOrder(order);
        if (effectedNum != 1) {
            throw new OrderOperationException("修改订单失败");
        }
        return new OrderExecution(OrderStateEnum.SUCCESS);
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
