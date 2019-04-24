package com.lovesickness.o2o.util;

import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.entity.OrderProductMap;
import com.lovesickness.o2o.entity.UserProductMap;

import java.util.ArrayList;
import java.util.List;

public class EntityTransformation {
    /**
     * 支付成功后 将订单信息 转换为 用户购买记录
     *
     * @param order
     * @return
     */
    public static List<UserProductMap> order2UserProductMaps(Order order) {
        List<OrderProductMap> orderProductMaps = order.getOrderProductMapList();
        List<UserProductMap> userProductMaps = new ArrayList<>(orderProductMaps.size());
        userProductMaps.forEach(userProductMap -> {
            userProductMap.setShop(order.getShop());
            userProductMap.setUser(order.getUser());
        });
        for (int i = 0; i < userProductMaps.size(); i++) {
            UserProductMap userProductMap = userProductMaps.get(i);
            OrderProductMap orderProductMap = orderProductMaps.get(i);
            userProductMap.setProduct(orderProductMap.getProduct());
            userProductMap.setPoint(orderProductMap.getProduct().getPoint());
            userProductMap.setCreateTime(orderProductMap.getUpdateTime());
        }
        return userProductMaps;
    }
}
