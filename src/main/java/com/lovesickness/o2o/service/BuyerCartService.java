package com.lovesickness.o2o.service;

import com.lovesickness.o2o.entity.BuyerCartItem;

import java.util.List;

public interface BuyerCartService {
    /**
     * 修改或者添加购物车
     *
     * @param userId      用户id
     * @param newCartItem 需要修改到购物车的订单项
     * @return 成功或失败
     */
    boolean updateItem(Long userId, BuyerCartItem newCartItem);

    /**
     * 商品总数
     *
     * @param userId 用户ID
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

    /**
     * 查询该用户的购物车列表
     *
     * @param userId 用户ID
     * @return 购物车列表
     */
    List<BuyerCartItem> getBuyerCart(Long userId);

    /**
     * 根据产品Id查询该产品在购物车中存在的情况
     *
     * @param userId    用户Id
     * @param productId 产品Id
     * @return 对应的产品在购物车中的信息
     */
    BuyerCartItem getBuyerCartByProductId(Long userId, Long productId);

}
