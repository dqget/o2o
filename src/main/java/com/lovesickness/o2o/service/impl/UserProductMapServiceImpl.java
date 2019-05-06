package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.UserProductMapDao;
import com.lovesickness.o2o.dao.UserShopMapDao;
import com.lovesickness.o2o.dto.UserProductMapExecution;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.UserProductMap;
import com.lovesickness.o2o.entity.UserShopMap;
import com.lovesickness.o2o.enums.UserProductMapStateEnum;
import com.lovesickness.o2o.exception.UserProductMapOperationException;
import com.lovesickness.o2o.service.UserProductMapService;
import com.lovesickness.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 懿
 */
@Service
public class UserProductMapServiceImpl implements UserProductMapService {
    @Autowired
    private UserProductMapDao userProductMapDao;
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    public UserProductMapExecution listUserProductMap(UserProductMap userProductMapCondition, Integer pageIndex, Integer pageSize) {
        //空值判断
        if (userProductMapCondition != null && pageIndex != null && pageSize != null) {
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            List<UserProductMap> userProductMaps = userProductMapDao.queryUserProductMapList(userProductMapCondition, beginIndex, pageSize);
            int count = userProductMapDao.queryUserProductMapCount(userProductMapCondition);
            return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS, userProductMaps, count);
        } else {
            //若有空值  返回错误代码
            return new UserProductMapExecution(UserProductMapStateEnum.NULL_USERPRODUCTMAP_INFO);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public UserProductMapExecution addUserProductMap(UserProductMap userProductMap) {
        //1.添加消费记录
        //2.积累积分  增加的积分数为serProductMap.getPoint()

        //消费记录、用户ID、商铺ID不能为空
        if (userProductMap != null && userProductMap.getUser().getUserId() != null
                && userProductMap.getShop().getShopId() != null) {
            userProductMap.setCreateTime(new Date());
            //添加消费记录
            int effectedNum = userProductMapDao.insertUserProductMap(userProductMap);
            if (effectedNum != 1) {
                throw new UserProductMapOperationException("添加消费记录失败");
            }
            if (userProductMap.getPoint() != null && userProductMap.getPoint() > 0) {
                //查询店铺内是否有该用户的积分记录
                UserShopMap oldUserShopMap = userShopMapDao.queryUserShopMap(
                        userProductMap.getUser().getUserId(),
                        userProductMap.getShop().getShopId());
                if (oldUserShopMap != null && oldUserShopMap.getUserShopId() != null) {
                    oldUserShopMap.setPoint(oldUserShopMap.getPoint() + userProductMap.getPoint());
                    effectedNum = userShopMapDao.updateUserShopMapPoint(oldUserShopMap);
                    if (effectedNum <= 0) {
                        throw new UserProductMapOperationException("更新积分失败");
                    }
                } else {
                    UserShopMap userShopMap = compactUserShopMap4Add(userProductMap.getUser().getUserId(), userProductMap.getShop().getShopId(), userProductMap.getPoint());
                    effectedNum = userShopMapDao.insertUserShopMap(userShopMap);
                    if (effectedNum <= 0) {
                        throw new UserProductMapOperationException("积分信息创建失败");
                    }
                }

            }
            return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS);
        } else {
            return new UserProductMapExecution(UserProductMapStateEnum.NULL_USERPRODUCTMAP_INFO);

        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public UserProductMapExecution batchAddUserProductMap(List<UserProductMap> userProductMaps) {
        try {
            for (UserProductMap userProductMap : userProductMaps) {
                UserProductMapExecution upme = addUserProductMap(userProductMap);
                if (upme.getState() != UserProductMapStateEnum.SUCCESS.getState()) {
                    return upme;
                }
            }
            return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS);
        } catch (Exception e) {
            throw new UserProductMapOperationException(e.getMessage());
        }

//        if (userProductMaps.size() > 0) {
//            int effectedNum = userProductMapDao.batchInsertUserProductMap(userProductMaps);
//            if (effectedNum != userProductMaps.size()) {
//                throw new UserProductMapOperationException("添加失败");
//            }
//            int pointCount = userProductMaps
//                    .stream()
//                    .mapToInt(UserProductMap::getPoint)
//                    .sum();
//            UserShopMap oldUserShopMap = userShopMapDao.queryUserShopMap(
//                    userProductMaps.get(0).getUser().getUserId(),
//                    userProductMaps.get(0).getShop().getShopId());
//            //店铺内存在该用户的积分信息
//            if (oldUserShopMap != null && oldUserShopMap.getUserShopId() != null) {
//                oldUserShopMap.setPoint(oldUserShopMap.getPoint() + pointCount);
//                effectedNum = userShopMapDao.updateUserShopMapPoint(oldUserShopMap);
//                if (effectedNum <= 0) {
//                    throw new UserProductMapOperationException("更新积分失败");
//                }
//            } else {
//                //店铺里不存在该用户的积分信息，需要新建一条用户与店铺的积分记录
//                UserShopMap userShopMap = compactUserShopMap4BatchAdd(userProductMaps, pointCount);
//                effectedNum = userShopMapDao.insertUserShopMap(userShopMap);
//                if (effectedNum <= 0) {
//                    throw new UserProductMapOperationException("积分信息创建失败");
//                }
//            }
//            return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS);
//        } else {
//            return new UserProductMapExecution(UserProductMapStateEnum.INNER_ERROR);
//        }
    }

//    private UserShopMap compactUserShopMap4BatchAdd(List<UserProductMap> userProductMaps, int pointCount) {
//        UserShopMap userShopMap = new UserShopMap();
//        userShopMap.setCreateTime(new Date());
//        userShopMap.setUser(userProductMaps.get(0).getUser());
//        userShopMap.setShop(userProductMaps.get(0).getShop());
//        userShopMap.setPoint(pointCount);
//        return userShopMap;
//    }

    private UserShopMap compactUserShopMap4Add(long userId, long shopId, int pointCount) {
        UserShopMap userShopMap = new UserShopMap();
        userShopMap.setCreateTime(new Date());
        PersonInfo buyer = new PersonInfo();
        buyer.setUserId(userId);
        Shop shop = new Shop();
        shop.setShopId(shopId);
        userShopMap.setUser(buyer);
        userShopMap.setShop(shop);
        userShopMap.setPoint(pointCount);
        return userShopMap;
    }
}
