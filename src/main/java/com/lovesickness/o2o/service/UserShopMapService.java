package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.UserShopMapExecution;
import com.lovesickness.o2o.entity.UserShopMap;
import com.lovesickness.o2o.exception.UserShopMapOperationException;

public interface UserShopMapService {
    /**
     * 根据查询条件查询UserShopMap列表
     *
     * @param userShopCondition 用户积分映射查询条件
     * @param pageIndex         页数
     * @param pageSize          每页显示数量
     * @return UserShopMapDTO
     */
    UserShopMapExecution queryUserShopMapList(UserShopMap userShopCondition, Integer pageIndex, Integer pageSize);

    /**
     * 根据用户id和商铺id查询该用户对应该商铺下的积分情况
     *
     * @param userId 用户id
     * @param shopId 商铺id
     * @return 积分情况
     */
    UserShopMap getUserShopMap(Long userId, Long shopId);

    /**
     * 添加用户在该店铺的积分
     *
     * @param userShopMap
     * @return
     * @throws UserShopMapOperationException
     */
    UserShopMapExecution addUserShopMap(UserShopMap userShopMap) throws UserShopMapOperationException;
}
