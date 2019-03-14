package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.UserAwardMapExecution;
import com.lovesickness.o2o.entity.UserAwardMap;

public interface UserAwardMapService {
    /**
     * 根据查询条件查询用户与奖品映射关系列表
     *
     * @param userAwardCondition 用户奖品映射查询条件
     * @param pageIndex          页数
     * @param pageSize           每页数量
     * @return 用户奖品映射DTO
     */
    UserAwardMapExecution queryUserAwardMapList(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize);

    /**
     * 用户领取奖品后，添加一条兑换信息
     *
     * @param userAwardMap 兑换信息映射对象
     * @return 用户奖品映射DTO
     */
    UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap);
}
