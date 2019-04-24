package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.UserProductMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 懿
 */

public interface UserProductMapDao {
    /**
     * 根据传入的条件  分页查询 顾客购买商品映射关系
     *
     * @param userProductCondition 查询条件
     * @param rowIndex
     * @param pageSize
     * @return 查询出的购买记录结果
     */
    List<UserProductMap> queryUserProductMapList
    (@Param("userProductCondition") UserProductMap userProductCondition,
     @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 根据传入条件查询总数 与分页查询配合使用
     *
     * @param userProductCondition 查询条件
     * @return 查询总数
     */
    int queryUserProductMapCount(@Param("userProductCondition") UserProductMap userProductCondition);

    /**
     * 增加 购买记录
     *
     * @param userProductMap 购买记录
     * @return 数据库添加数量
     */
    int insertUserProductMap(UserProductMap userProductMap);

    /**
     * 批量添加购买记录
     *
     * @param userProductMapList 购买记录列表
     * @return 数据库添加数量
     */
    int batchInsertUserProductMap(List<UserProductMap> userProductMapList);

}

