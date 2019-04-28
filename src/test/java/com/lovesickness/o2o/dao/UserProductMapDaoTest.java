package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.UserProductMap;
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
public class UserProductMapDaoTest {
    @Autowired
    private UserProductMapDao userProductMapDao;

    @Test
    public void testAInsertUserProductMap() {
        UserProductMap userProductMap = new UserProductMap();
        userProductMap.setCreateTime(new Date());
//        userProductMap.setPoint(1);
        PersonInfo buyer = new PersonInfo();
        buyer.setUserId(6L);
        userProductMap.setUser(buyer);
        Shop shop = new Shop();
        shop.setShopId(12L);
        userProductMap.setShop(shop);
//        PersonInfo operator = new PersonInfo();
//        operator.setUserId(1L);
//        userProductMap.setOperator(operator);
        Product product = new Product();
        product.setProductId(5L);
        userProductMap.setProduct(product);
        int effectedNum = userProductMapDao.insertUserProductMap(userProductMap);
        Assert.assertEquals(1, effectedNum);
    }

    @Test
    public void testBQueryUserProductMapList() {
        UserProductMap userProductMap = new UserProductMap();
        List<UserProductMap> userProductMaps = userProductMapDao.queryUserProductMapList(userProductMap, 0, 99);
        userProductMaps.forEach(System.out::println);
        int count = userProductMapDao.queryUserProductMapCount(userProductMap);
        Assert.assertEquals(count,userProductMaps.size());
    }

}
