package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.UserAwardMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAwardDao {
    /**
     * 根据传入的条件查询分页返回用户兑换奖品记录的列表信息
     *
     * @param userAwardCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<UserAwardMap> queryUserAwardMapList(
            @Param("userAwardCondition") UserAwardMap userAwardCondition,
            @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 根据传入的条件查询分页返回用户兑换奖品记录总数
     *
     * @param userAwardCondition
     * @return
     */
    int queryUserAwardMapCount(
            @Param("userAwardCondition") UserAwardMap userAwardCondition);


    UserAwardMap queryUserAwardMapById(long userAwardId);


    int insertUserAwardMap(UserAwardMap userAwardMap);


    int updateUserAwardMap(UserAwardMap userAwardMap);
}
