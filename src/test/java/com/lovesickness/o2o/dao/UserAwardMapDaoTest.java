package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Award;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.UserAwardMap;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserAwardMapDaoTest {
    @Autowired
    private UserAwardMapDao userAwardMapDao;

    @Test
    public void testAInsertUserAwardMap() {
        UserAwardMap userAwardMap = new UserAwardMap();
        userAwardMap.setCreateTime(new Date());
        userAwardMap.setPoint(1);
        userAwardMap.setUsedStatus(0);
        PersonInfo user = new PersonInfo();
        user.setUserId(6L);
        userAwardMap.setUser(user);
        Award award = new Award();
        award.setAwardId(2L);
        userAwardMap.setAward(award);
        Shop shop = new Shop();
        shop.setShopId(12L);
        userAwardMap.setShop(shop);
        PersonInfo operator = new PersonInfo();
        operator.setUserId(1L);
        userAwardMap.setOperator(operator);
        int effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testBUpdateUserAwardMap() {
        UserAwardMap userAwardMap = new UserAwardMap();
        userAwardMap.setUsedStatus(1);
        userAwardMap.setUserAwardId(1L);
        PersonInfo user = new PersonInfo();
        user.setUserId(6L);
        userAwardMap.setUser(user);
        int effectedNum = userAwardMapDao.updateUserAwardMap(userAwardMap);
        assertEquals(effectedNum, 1);
    }

    @Test
    public void testCQueryUserAwardMapById() {
        UserAwardMap userAwardMap = userAwardMapDao.queryUserAwardMapById(1L);
        System.out.println(userAwardMap);
    }

    @Test
    public void testDQqueryUserAwardMapList() {
        UserAwardMap userAwardMap = new UserAwardMap();
        userAwardMap.setPoint(1);
        userAwardMap.setUsedStatus(1);
        PersonInfo user = new PersonInfo();
//        user.setUserId(6L);
        user.setName("");
        userAwardMap.setUser(user);
        List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 99);
        int effectedNum = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
        userAwardMapList.forEach(System.out::println);
        assertEquals(effectedNum, userAwardMapList.size());
    }
}
