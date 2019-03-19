package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.OrderProductMap;

import java.util.List;

public interface OrderProductMapDao {
    /**
     * 批量添加订单项
     *
     * @param orderProductMapList
     * @return
     */
    int batchInsertOrderProductMap(List<OrderProductMap> orderProductMapList);

}
