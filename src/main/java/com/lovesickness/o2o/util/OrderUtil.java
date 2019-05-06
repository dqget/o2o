package com.lovesickness.o2o.util;

import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;

public class OrderUtil {
    public static final int ORDER_ALL = 0;
    public static final int ORDER_NOT_PAY = 1;
    public static final int ORDER_NOT_SHIP = 2;
    public static final int ORDER_NOT_RECEIPT = 3;
    public static final int ORDER_NOT_EVA = 4;
    public static final int ORDER_BUSINESS_SUCCESS = 5;

    /**
     * @param state 0全部 1待付款、2待发货、3待收货 4待评价
     */
    public static Order compactOrderCondition(PersonInfo user, Shop shop, int state) {
        Order order = new Order();
        order.setUser(user);
        order.setShop(shop);
        switch (state) {
            case ORDER_ALL:
                break;
            case ORDER_NOT_PAY:
                order.setIsPay(0);
                order.setIsShip(0);
                order.setIsReceipt(0);
                break;
            case ORDER_NOT_SHIP:
                order.setIsPay(1);
                order.setIsShip(0);
                order.setIsReceipt(0);
                break;
            case ORDER_NOT_RECEIPT:
                order.setIsPay(1);
                order.setIsShip(1);
                order.setIsReceipt(0);
                break;
            case ORDER_BUSINESS_SUCCESS:
                order.setIsPay(1);
                order.setIsShip(1);
                order.setIsReceipt(1);
                break;
            default:
                break;
        }
        return order;
    }
}
