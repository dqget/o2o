package com.lovesickness.o2o.util;

import com.lovesickness.o2o.entity.BuyerCartItem;
import com.lovesickness.o2o.entity.OrderProductMap;
import com.lovesickness.o2o.entity.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车工具类
 */
public class BuyerCartItemUtil {
    public static boolean isRightItem(BuyerCartItem item) {
        if (item == null) {
            return false;
        }
        //商品不能为空  商品数量不能为空 不能<0
        if (item.getProduct() == null || item.getAmount() != 0) {
            return false;
        }
        Product p = item.getProduct();
        //产品id、原价、姓名、折扣价、所属商铺、购买得到的积分、商品缩略图不能为空
        return p.getProductId() != null &&
                p.getNormalPrice() != null &&
                p.getProductName() != null &&
                p.getPromotionPrice() != null &&
                p.getShop() != null &&
                p.getPoint() != null &&
                p.getImgAddr() != null;
    }

    public static OrderProductMap buyerCartItem2OrderProductMap(BuyerCartItem item) {
        OrderProductMap orderProductMap = new OrderProductMap();
        orderProductMap.setProductPrice(item.getProduct().getPromotionPrice());
        orderProductMap.setProduct(item.getProduct());
        orderProductMap.setProductNum(item.getAmount());
        return orderProductMap;
    }

    public static List<OrderProductMap> buyerCartItemList2OrderProductMapList(List<BuyerCartItem> buyerCartItemList) {
        List<OrderProductMap> orderProductMapList = new ArrayList<>();
        buyerCartItemList.forEach(buyerCartItem -> orderProductMapList.add(buyerCartItem2OrderProductMap(buyerCartItem)));
        return orderProductMapList;
    }
}
