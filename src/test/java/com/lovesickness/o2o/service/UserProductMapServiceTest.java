package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.UserProductMapExecution;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.UserProductMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserProductMapServiceTest {
    @Autowired
    private UserProductMapService userProductMapService;


    @Test
    public void testABatchAddUserProductMap() {
        List<UserProductMap> userProductMaps = new ArrayList<>();
        UserProductMap userProductMap1 = new UserProductMap();
        userProductMap1.setCreateTime(new Date());
        userProductMap1.setPoint(1);
        PersonInfo buyer1 = new PersonInfo();
        buyer1.setUserId(6L);
        userProductMap1.setUser(buyer1);
        Shop shop1 = new Shop();
        shop1.setShopId(12L);
        userProductMap1.setShop(shop1);
        PersonInfo operator1 = new PersonInfo();
        operator1.setUserId(1L);
//        userProductMap1.setOperator(operator1);
        Product product1 = new Product();
        product1.setProductId(5L);
        userProductMap1.setProduct(product1);
        UserProductMap userProductMap = new UserProductMap();
        userProductMap.setCreateTime(new Date());
        userProductMap.setPoint(1);
        PersonInfo buyer = new PersonInfo();
        buyer.setUserId(6L);
        userProductMap.setUser(buyer);
        Shop shop = new Shop();
        shop.setShopId(12L);
        userProductMap.setShop(shop);
        PersonInfo operator = new PersonInfo();
        operator.setUserId(1L);
//        userProductMap.setOperator(operator);
        Product product = new Product();
        product.setProductId(5L);
        userProductMap.setProduct(product);

        userProductMaps.add(userProductMap);
        userProductMaps.add(userProductMap1);
        UserProductMapExecution upme = userProductMapService.batchAddUserProductMap(userProductMaps);
        System.out.println("0----------------");
    }
}
