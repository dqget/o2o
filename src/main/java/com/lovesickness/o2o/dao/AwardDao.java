package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Award;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwardDao {
    /**
     * 依据传入进来的查询条件分页显示奖品信息列表
     *
     * @param award    查询条件
     * @param rowIndex 查询开始行数
     * @param pageSize 所需要的行数
     * @return 列表
     */
    List<Award> queryAwardList(@Param("awardCondition") Award award,
                               @Param("rowIndex") int rowIndex,
                               @Param("pageSize") int pageSize);

    int queryAwardCount(@Param("awardCondition") Award awardCondition);

    Award queryAwardByAwardId(long awardId);

    /**
     * 添加奖品
     *
     * @param award 奖品实体类
     * @return 修改数据库行数
     */
    int insertAward(Award award);

    int updateAward(Award award);

    int deleteAward(@Param("awardId") long awardId, @Param("shopId") long shopId);
}
