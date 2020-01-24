package com.lovesickness.o2o.service;

import com.lovesickness.o2o.entity.ProductSellDaily;

import java.util.Date;
import java.util.List;

/**
 * @author 懿
 */
public interface ProductSellDailyService {
    /**
     * 每日定时对搜有店铺的商品销售进行统计
     */
    void dailyCalculate();

    /**
     * 根据条件查询  返回商品日销售量的统计
     */
    List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime);

}
