package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.OrderExecution;
import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.entity.OrderProductMap;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.exception.OrderOperationException;

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

    /**
     * 订单查询 ：根据名字模糊查询  待付款  待发货   待收货  评价
     *
     * @param order     订单信息
     * @param pageIndex 查询当前页数
     * @param pageSize  一页显示数
     * @param keyWord   关键字 根据产品名模糊查询
     * @return 订单信息及查询总数
     */
    OrderExecution getOrderList(Order order, String keyWord, Integer pageIndex, Integer pageSize);

    /**
     * 单查询 ：根据名字模糊查询 未评价
     *
     * @param userId    用户Id
     * @param pageIndex 查询当前页数
     * @param pageSize  一页显示数
     * @param keyWord   关键字 根据产品名模糊查询
     * @return 订单信息及查询总数
     */
    OrderExecution getOrderNotEvaList(Long userId, Long shopId, String keyWord, Integer pageIndex, Integer pageSize);

    OrderExecution modifyOrderByUser(Order order) throws OrderOperationException;

    OrderExecution modifyOrderByShop(Order order) throws OrderOperationException;
}
