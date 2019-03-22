package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * @param order 订单信息
     * @return 修改行数
     */
    int insertOrder(Order order);

    /**
     * 查询对应的订单
     *
     * @param order    订单信息
     * @param rowIndex 开始行数
     * @param pageSize 获取行数
     * @return 订单列表
     */
    List<Order> queryOrderList(@Param("orderCondition") Order order,
                               @Param("rowIndex") int rowIndex,
                               @Param("pageSize") int pageSize);

    /**
     * 查询对应的订单总数
     *
     * @param order 订单信息
     * @return 总数
     */
    int queryOrderCount(@Param("orderCondition") Order order);

}
