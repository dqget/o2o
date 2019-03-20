package com.lovesickness.o2o.service;

import com.lovesickness.o2o.entity.BuyerCartItem;

import java.util.List;

public interface BuyerCartService {
    /**
     * 添加购物车
     *
     * @param userId      用户id
     * @param newCartItem 需要添加到购物车的订单项
     * @return 成功或失败
     */
    boolean addItem(Long userId, BuyerCartItem newCartItem);

    /**
     * 商品总数
     *
     * @return 商品数量和
     */
    Integer getProductAmount(Long userId);

    /**
     * 总价值(折扣价的和)
     *
     * @param userId 用户Id
     * @return 商品总价值
     */
    Integer getProductPrice(Long userId);

    /**
     * 总原价值(原价的和)
     *
     * @param userId 用户Id
     * @return 商品总原价值
     */
    Integer getProductNormalPrice(Long userId);

    List<BuyerCartItem> getBuyerCart(Long userId);
}
