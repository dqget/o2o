package com.lovesickness.o2o.service;

import com.lovesickness.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    public static final String SHOPCATEGORYLISTKEY = "shopcategorylist";

    /**
     * 根据查询条件获取店铺标题列表
     *
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
