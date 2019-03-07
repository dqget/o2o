package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 懿
 */
@Repository
public interface ShopDao {
    /**
     * 新增店铺
     *
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺
     *
     * @param shop
     * @return
     */
    int updateShop(Shop shop);

    /***
     * 通过shopId 查询店铺
     *
     * @param shopId
     * @return
     */
    Shop queryByShopId(long shopId);

    /***
     * 分页查询店铺，可输入 店铺名 店铺状态 店铺类别 区域Id owner
     *
     * @param shopCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex,
                             @Param("pageSize") int pageSize);

    /***
     *  分页查询店铺总数
     * @param shopCondition
     * @return
     */
    Integer queryShopCount(@Param("shopCondition") Shop shopCondition);

}