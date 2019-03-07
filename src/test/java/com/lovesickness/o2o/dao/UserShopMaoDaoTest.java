package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.UserShopMap;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserShopMaoDaoTest {
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Test
    public void testAInsertUserShopMap() {
        UserShopMap userShopMap = new UserShopMap();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(6L);
        Shop shop = new Shop();
        shop.setShopId(12L);
        userShopMap.setUser(personInfo);
        userShopMap.setShop(shop);
        userShopMap.setCreateTime(new Date());
        userShopMap.setPoint(100000);
        int effectedNum = userShopMapDao.insertUserShopMap(userShopMap);
        Assert.assertEquals(effectedNum, 1);
    }

    @Test
    public void testBQueryUserShopMapList() {
        UserShopMap userShopMap = new UserShopMap();
        List<UserShopMap> userShopMaps = userShopMapDao.queryUserShopMapList(userShopMap,0,2);
        int effectedNum = userShopMapDao.queryUserShopMapCount(userShopMap);
        System.out.println(effectedNum+":"+userShopMaps);

    }

    @Test
    public void testCQueryUserShopMap() {
        UserShopMap userShopMap = userShopMapDao.queryUserShopMap(6L,12L);
        System.out.println(userShopMap);

    }

    @Test
    public void testDupdateUserShopMapPoint() {
        UserShopMap userShopMap = userShopMapDao.queryUserShopMap(6L,12L);
        System.out.println(userShopMap);
        userShopMap.setPoint(11000000);
        int effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
        userShopMap = userShopMapDao.queryUserShopMap(6L,12L);
        System.out.println(userShopMap);

    }
}
