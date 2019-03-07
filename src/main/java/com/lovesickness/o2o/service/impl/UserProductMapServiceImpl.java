package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.UserProductMapDao;
import com.lovesickness.o2o.dto.UserProductMapExecution;
import com.lovesickness.o2o.entity.UserProductMap;
import com.lovesickness.o2o.enums.UserProductMapStateEnum;
import com.lovesickness.o2o.service.UserProductMapService;
import com.lovesickness.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 懿
 */
@Service
public class UserProductMapServiceImpl implements UserProductMapService {
    @Autowired
    private UserProductMapDao userProductMapDao;

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
}
