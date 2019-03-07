package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.dto.ShopExecution;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.exception.ShopOperationException;

public interface ShopService {
    /***
     * 注册店铺
     * @param shop
     * @param image
     * @return
     * @throws ShopOperationException
     */
    ShopExecution addShop(Shop shop, ImageHolder image) throws ShopOperationException;

    /***
     *
     * @param shopId
     * @return ShopExecution
     * @throws ShopOperationException
     */
    Shop queryShopById(long shopId) throws ShopOperationException;

    /***
     * 更新店铺信息，包括图片处理
     * @param shop
     * @param image
     * @return Shop
     * @throws ShopOperationException
     */
    ShopExecution modifyShop(Shop shop, ImageHolder image) throws ShopOperationException;

    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
}

