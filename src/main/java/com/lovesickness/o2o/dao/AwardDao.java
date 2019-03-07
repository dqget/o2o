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
     * @param award
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Award> queryAwardList(@Param("awardCondition") Award award,
                               @Param("rowIndex") int rowIndex,
                               @Param("pageSize") int pageSize);

    int queryAwardCount(@Param("awardCondition") Award awardCondition);

    Award queryAwardByAwardId(long awardId);

    //添加奖品
    int insertAward(Award award);

    int updateAward(Award award);

    int deleteAward(@Param("awardId") long awardId, @Param("shopId") long shopId);
}
