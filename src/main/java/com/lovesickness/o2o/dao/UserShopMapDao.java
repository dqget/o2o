package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.UserShopMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserShopMapDao {
    /**
     * 根据查询条件分页返回用户店铺积分列表
     *
     * @param userShopCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<UserShopMap> queryUserShopMapList(
            @Param("userShopCondition") UserShopMap userShopCondition,
            @Param("rowIndex") int rowIndex,
            @Param("pageSize") int pageSize);

    /**
     * 根据查询条件返回用户店铺积分列表总数
     *
     * @param userShopCondition
     * @return
     */
    int queryUserShopMapCount(@Param("userShopCondition") UserShopMap userShopCondition);

    /**
     * 根据传入的用户Id和shopId查询该用户在某个店铺下的积分信息
     *
     * @param userId
     * @param shopId
     * @return
     */
    UserShopMap queryUserShopMap(@Param("userId") long userId,
                                 @Param("shopId") long shopId);

    /**
     * 添加
     *
     * @param userShopMap
     * @return
     */
    int insertUserShopMap(UserShopMap userShopMap);
    /**
     * 修改
     *
     * @param userShopMap
     * @return
     */
    int updateUserShopMapPoint(UserShopMap userShopMap);
}
