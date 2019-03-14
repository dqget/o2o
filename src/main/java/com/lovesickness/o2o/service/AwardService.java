package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.AwardExecution;
import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.entity.Award;

public interface AwardService {
    /**
     * 根据查询条件查询奖品列表
     *
     * @param awardCondition 查询条件
     * @param pageIndex      开始页数
     * @param pageSize       每页数量
     * @return 奖品DTO
     */
    AwardExecution queryAwardList(Award awardCondition, Integer pageIndex, Integer pageSize);

    /**
     * 根据Id查询奖品
     *
     * @param awardId 奖品Id
     * @return 奖品信息
     */
    Award getAwardById(Long awardId);

    /**
     * 添加一条奖品信息
     *
     * @param award     奖品信息
     * @param thumbnail 图片信息
     * @return 失败或成功奖品DTO
     */
    AwardExecution addAward(Award award, ImageHolder thumbnail);

    /**
     * 修改一条奖品信息
     *
     * @param award     奖品信息
     * @param thumbnail 图片信息
     * @return 失败或成功奖品DTO
     */
    AwardExecution modifyAward(Award award, ImageHolder thumbnail);
}
