package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.UserShopMapDao;
import com.lovesickness.o2o.dto.UserShopMapExecution;
import com.lovesickness.o2o.entity.UserShopMap;
import com.lovesickness.o2o.enums.UserShopMapStateEnum;
import com.lovesickness.o2o.exception.UserShopMapOperationException;
import com.lovesickness.o2o.service.UserShopMapService;
import com.lovesickness.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserShopMapServiceImpl implements UserShopMapService {
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    public UserShopMapExecution queryUserShopMapList(UserShopMap userShopCondition, Integer pageIndex, Integer pageSize) {
        UserShopMapExecution ue;
        if (userShopCondition != null && pageIndex != null && pageSize != null) {
            ue = new UserShopMapExecution(UserShopMapStateEnum.SUCCESS);
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            ue.setUserShopMapList(userShopMapDao.queryUserShopMapList(userShopCondition, beginIndex, pageSize));
            ue.setCount(userShopMapDao.queryUserShopMapCount(userShopCondition));
            return ue;
        } else {
            throw new UserShopMapOperationException(UserShopMapStateEnum.EMPTY.getStateInfo());
        }

    }

    @Override
    public UserShopMap getUserShopMap(Long userId, Long shopId) {
        if (userId != null && shopId != null) {
            return userShopMapDao.queryUserShopMap(userId, shopId);
        } else {
            throw new UserShopMapOperationException(UserShopMapStateEnum.EMPTY.getStateInfo());
        }
    }

    @Override
    public UserShopMapExecution addUserShopMap(UserShopMap userShopMap) throws UserShopMapOperationException {
        UserShopMap oldUserShopMap = userShopMapDao.queryUserShopMap(
                userShopMap.getUser().getUserId(),
                userShopMap.getShop().getShopId());
        int effectedNum = 0;
        if (oldUserShopMap != null) {
            oldUserShopMap.setPoint(userShopMap.getPoint() + oldUserShopMap.getPoint());
            effectedNum = userShopMapDao.updateUserShopMapPoint(oldUserShopMap);

        } else {
            effectedNum = userShopMapDao.insertUserShopMap(userShopMap);
        }
        if (effectedNum != 1) {
            throw new UserShopMapOperationException("添加用户积分失败");
        }
        return new UserShopMapExecution(UserShopMapStateEnum.SUCCESS);
    }
}
