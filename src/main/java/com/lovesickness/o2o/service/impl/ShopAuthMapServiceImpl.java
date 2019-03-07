package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.ShopAuthMapDao;
import com.lovesickness.o2o.dto.ShopAuthMapExecution;
import com.lovesickness.o2o.entity.ShopAuthMap;
import com.lovesickness.o2o.enums.ShopAuthStateEnum;
import com.lovesickness.o2o.exception.ShopAuthMapOperationException;
import com.lovesickness.o2o.service.ShopAuthMapService;
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
public class ShopAuthMapServiceImpl implements ShopAuthMapService {
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Override
    public ShopAuthMapExecution getShopAuthMapListByShopId(Long shopId, Integer pageIndex, Integer pageSize) {
        if (shopId != null && pageIndex != null && pageSize != null) {
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(shopId, beginIndex, pageSize);
            int count = shopAuthMapDao.queryShopAuthMapCountByShopId(shopId);
            ShopAuthMapExecution se = new ShopAuthMapExecution();
            se.setShopAuthMapList(shopAuthMapList);
            se.setCount(count);
            return se;
        } else {
            return null;
        }
    }

    @Override
    public ShopAuthMap getShopAuthMapById(Long shopAuthMapId) {
        return shopAuthMapDao.queryShopAuthMapById(shopAuthMapId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
        if (shopAuthMap != null && shopAuthMap.getShop() != null && shopAuthMap.getShop().getShopId() != null && shopAuthMap.getEmployee() != null && shopAuthMap.getEmployee().getUserId() != null) {
            shopAuthMap.setCreateTime(new Date());
            shopAuthMap.setLastEditTime(new Date());
            shopAuthMap.setEnableStatus(1);
            shopAuthMap.setTitleFlag(1);
            try {
                int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                if (effectedNum != 1) {
                    throw new ShopAuthMapOperationException("添加授权失败");
                }
                return new ShopAuthMapExecution(ShopAuthStateEnum.SUCCESS, shopAuthMap);
            } catch (RuntimeException e) {
                throw new ShopAuthMapOperationException("添加授权失败" + e.getMessage());
            }
        } else {
            return new ShopAuthMapExecution(ShopAuthStateEnum.NULL_SHOPAUTH_INFO);

        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
        if (shopAuthMap == null || shopAuthMap.getShopAuthId() == null) {
            return new ShopAuthMapExecution(ShopAuthStateEnum.NULL_SHOPAUTH_INFO);
        } else {
            try {
                int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
                if (effectedNum != 1) {
                    return new ShopAuthMapExecution(ShopAuthStateEnum.INNER_ERROR);
                }
                return new ShopAuthMapExecution(ShopAuthStateEnum.SUCCESS, shopAuthMap);
            } catch (RuntimeException e) {
                throw new ShopAuthMapOperationException("修改授权失败" + e.getMessage());
            }
        }
    }
}
