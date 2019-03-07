package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.ShopAuthMap;
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
public class ShopAuthMapDaoTest {
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Test
    public void testAInsertShopAuthMap() {
        ShopAuthMap shopAuthMap = new ShopAuthMap();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);
        Shop shop = new Shop();
        shop.setShopId(12L);
        shopAuthMap.setEmployee(personInfo);
        shopAuthMap.setShop(shop);
        shopAuthMap.setCreateTime(new Date());
        shopAuthMap.setLastEditTime(new Date());
        shopAuthMap.setTitle("老板");
        shopAuthMap.setTitleFlag(1);
        shopAuthMap.setEnableStatus(1);
        int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
        Assert.assertEquals(1, effectedNum);
    }

    @Test
    public void testBUpdateShopAuthMap() {
        ShopAuthMap shopAuthMap = new ShopAuthMap();
        shopAuthMap.setShopAuthId(1L);
        shopAuthMap.setLastEditTime(new Date());
        shopAuthMap.setTitle("老板娘~");
        shopAuthMap.setTitleFlag(2);
        shopAuthMap.setEnableStatus(2);
        int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
        Assert.assertEquals(1, effectedNum);
    }

    @Test
    public void testCDeleteShopAuthMap() {
        int effectedNum = shopAuthMapDao.deleteShopAuthMap(1L);
        Assert.assertEquals(1, effectedNum);
    }

    @Test
    public void testDQueryShopAuthMapById() {
        ShopAuthMap shopAuthMap = shopAuthMapDao.queryShopAuthMapById(2L);
        System.out.println(shopAuthMap);
    }

    @Test
    public void testEQueryShopAuthMapListByShopId() {
        List<ShopAuthMap> shopAuthMaps = shopAuthMapDao.queryShopAuthMapListByShopId(12L, 0, 2);
        int effectedNum = shopAuthMapDao.queryShopAuthMapCountByShopId(12L);
        shopAuthMaps.forEach(System.out::println);
        System.out.println(effectedNum);
    }
}
