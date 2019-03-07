package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.ProductSellDaily;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ProductSellDailyDao {
    /**
     * 根据查询条件返回商品日销售的统计列表
     *
     * @param productSellDailyCondition
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductSellDaily> queryProductSellDailyList(
            @Param("productSellDailyCondition") ProductSellDaily productSellDailyCondition,
            @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 统计平台当天销售过的商品的日销量
     *
     * @return
     */
    int insertProductSellDaily();

    /**
     * 统计平台当天未销售的商品
     *
     * @return
     */
    int insertDefaultProductSellDaily();

}
