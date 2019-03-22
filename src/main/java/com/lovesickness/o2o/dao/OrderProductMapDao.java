package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.OrderProductMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderProductMapDao {
    /**
     * 批量添加订单项
     *
     * @param orderProductMapList
     * @return
     */
    int batchInsertOrderProductMap(List<OrderProductMap> orderProductMapList);

    /**
     * 根据Id查询订单项信息
     *
     * @param orderProductMapId 订单项Id
     * @return 订单项信息
     */
    OrderProductMap queryOrderProductMapById(Long orderProductMapId);

    /**
     * 查询未评价的订单项
     *
     * @param orderProductMapCondition 订单项信息
     * @param rowIndex                 开始行数
     * @param pageSize                 获取行数
     * @return 订单项列表
     */
    List<OrderProductMap> queryOrderProductMapList(
            @Param("orderProductMapCondition") OrderProductMap orderProductMapCondition,
            @Param("rowIndex") int rowIndex,
            @Param("pageSize") int pageSize);

    /**
     * 查询对应的订单项总数
     *
     * @param orderProductMapCondition 订单项信息
     * @return 总数
     */
    int queryOrderProductMapCount(@Param("orderProductMapCondition") OrderProductMap orderProductMapCondition);

}
