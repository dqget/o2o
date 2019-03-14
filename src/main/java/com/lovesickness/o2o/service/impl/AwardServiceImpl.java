package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.AwardDao;
import com.lovesickness.o2o.dto.AwardExecution;
import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.entity.Award;
import com.lovesickness.o2o.enums.AwardStateEnum;
import com.lovesickness.o2o.exception.AwardOperationException;
import com.lovesickness.o2o.service.AwardService;
import com.lovesickness.o2o.util.ImageUtile;
import com.lovesickness.o2o.util.PageCalculator;
import com.lovesickness.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AwardServiceImpl implements AwardService {
    @Autowired
    private AwardDao awardDao;

    @Override
    public AwardExecution queryAwardList(Award awardCondition, Integer pageIndex, Integer pageSize) {
        AwardExecution ae;
        if (awardCondition != null && pageIndex != null && pageSize != null) {
            ae = new AwardExecution(AwardStateEnum.SUCCESS);
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            ae.setAwardList(awardDao.queryAwardList(awardCondition, beginIndex, pageSize));
            ae.setCount(awardDao.queryAwardCount(awardCondition));
            return ae;
        } else {
            throw new AwardOperationException(AwardStateEnum.EMPTY.getStateInfo());
        }

    }

    @Override
    public Award getAwardById(Long awardId) {
        if (awardId != null) {
            return awardDao.queryAwardByAwardId(awardId);
        } else {
            throw new AwardOperationException("awardId不能为空");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public AwardExecution addAward(Award award, ImageHolder thumbnail) {
        if (award != null && award.getShopId() != null && award.getAwardName() != null) {
            award.setCreateTime(new Date());
            award.setLastEditTime(new Date());
            award.setEnableStatus(1);
            if (thumbnail != null) {
                addThumbnail(award, thumbnail);
            }
            try {
                int effectedNum = awardDao.insertAward(award);
                if (effectedNum != 1) {
                    throw new AwardOperationException("创建奖品失败");
                }
            } catch (Exception e) {
                throw new AwardOperationException("创建奖品失败: " + e.toString());
            }
            return new AwardExecution(AwardStateEnum.SUCCESS, award);
        } else {
            //product为空
            throw new AwardOperationException(AwardStateEnum.EMPTY.getStateInfo());
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public AwardExecution modifyAward(Award award, ImageHolder thumbnail) {
        if (award != null && award.getShopId() != null) {
            if (thumbnail != null) {
                Award tempAward = awardDao.queryAwardByAwardId(award.getAwardId());
                String awardImg = tempAward.getAwardImg();
                if (awardImg != null) {
                    ImageUtile.deleteFileOrPath(tempAward.getAwardImg());
                }
                addThumbnail(award, thumbnail);
            }
            try {
                award.setLastEditTime(new Date());
                int effectedNum = awardDao.updateAward(award);
                if (effectedNum != 1) {
                    throw new AwardOperationException("更新奖品信息失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new AwardOperationException("modifyAward error : " + e.toString());
            }
            return new AwardExecution(AwardStateEnum.SUCCESS, award);
        } else {
            //award为空
            throw new AwardOperationException(AwardStateEnum.EMPTY.getStateInfo());
        }
    }

    /**
     * 添加缩略图
     *
     * @param award
     * @param thumbnail
     */
    private void addThumbnail(Award award, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(award.getShopId());
        String thumbnailAddr = ImageUtile.generateThumbnails(thumbnail, dest);
        award.setAwardImg(thumbnailAddr);
    }
}
