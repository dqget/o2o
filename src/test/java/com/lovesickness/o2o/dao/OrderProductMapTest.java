package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.OrderProductMap;
import com.lovesickness.o2o.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderProductMapTest {
    @Autowired
    private OrderProductMapDao orderProductMapDao;
    @Autowired
    private ProductDao productDao;

    @Test
    public void testAInsertOrder() {
        List<OrderProductMap> orderProductMaps = new ArrayList<>();
        OrderProductMap orderProductMap = new OrderProductMap();
        Product product = productDao.queryProductByProductId(4L);
        orderProductMap.setProduct(product);
        orderProductMap.setProductNum(2);
        orderProductMap.setProductPrice(product.getPromotionPrice());
        orderProductMap.setCreateTime(new Date());
        orderProductMap.setUpdateTime(new Date());
        orderProductMap.setOrderId(1L);
        orderProductMaps.add(orderProductMap);
        product = productDao.queryProductByProductId(5L);
        orderProductMap.setProduct(product);
        orderProductMaps.add(orderProductMap);
        int effectedNum = orderProductMapDao.batchInsertOrderProductMap(orderProductMaps);
        assertEquals(2,effectedNum);

    }

}
