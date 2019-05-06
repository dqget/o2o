package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.UserAwardMapDao;
import com.lovesickness.o2o.dao.UserShopMapDao;
import com.lovesickness.o2o.dto.UserAwardMapExecution;
import com.lovesickness.o2o.entity.UserAwardMap;
import com.lovesickness.o2o.entity.UserShopMap;
import com.lovesickness.o2o.enums.UserAwardMapStateEnum;
import com.lovesickness.o2o.enums.UserShopMapStateEnum;
import com.lovesickness.o2o.exception.UserAwardMapOperationException;
import com.lovesickness.o2o.service.UserAwardMapService;
import com.lovesickness.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {
    @Autowired
    private UserAwardMapDao userAwardMapDao;
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    public UserAwardMapExecution queryUserAwardMapList(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize) {
        UserAwardMapExecution uae;
        if (userAwardCondition != null && pageIndex != null && pageSize != null) {
            uae = new UserAwardMapExecution();
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            uae.setCount(userAwardMapDao.queryUserAwardMapCount(userAwardCondition));
            uae.setUserAwardMapList(userAwardMapDao.queryUserAwardMapList(userAwardCondition, beginIndex, pageSize));
            return uae;
        } else {
            throw new UserAwardMapOperationException(UserShopMapStateEnum.EMPTY.getStateInfo());
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) {
        if (userAwardMap != null && userAwardMap.getAward() != null && userAwardMap.getUser() != null && userAwardMap.getShop() != null) {
            userAwardMap.setCreateTime(new Date());
            userAwardMap.setUsedStatus(0);
            int effectedNum;
            //该奖品需要积分，则需要积分抵扣
            if (userAwardMap.getPoint() != null && userAwardMap.getPoint() > 0) {
                UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userAwardMap.getUser().getUserId(), userAwardMap.getShop().getShopId());
                //本用户在该店铺有积分，且兑换奖品需要的积分需要大于所拥有的积分
                if (userShopMap != null && userShopMap.getPoint() >= userAwardMap.getPoint()) {
                    //积分抵扣
                    userShopMap.setPoint(userShopMap.getPoint() - userAwardMap.getPoint());
                    effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
                    if (effectedNum <= 0) {
                        throw new UserAwardMapOperationException(UserShopMapStateEnum.INNER_ERROR.getStateInfo());
                    }
                } else {
                    throw new UserAwardMapOperationException("在本店铺没有足够的积分");
                }
            }
            effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
            if (effectedNum <= 0) {
                throw new UserAwardMapOperationException("奖品领取失败");
            }
            return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS);
        } else {
            throw new UserAwardMapOperationException(UserAwardMapStateEnum.NULL_USER_AWARD_MAP.getStateInfo());
        }

    }
}
