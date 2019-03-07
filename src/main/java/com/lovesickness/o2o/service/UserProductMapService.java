package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.UserProductMapExecution;
import com.lovesickness.o2o.entity.UserProductMap;

/**
 * @author 懿
 */
public interface UserProductMapService {
    /**
     * 通过传入的查询条件分页列出用户的消费信息列表
     */
    UserProductMapExecution listUserProductMap(UserProductMap userProductMapCondition, Integer pageIndex, Integer pageSize);

}
