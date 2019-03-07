package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.ProductCategory;
import org.junit.Assert;
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
public class ProductCategoryTest {
    @Autowired
    ProductCategoryDao dao;

    @Test
    public void testQueryProductCategoryList() {
        List<ProductCategory> list = dao.queryProductCategoryByShopId(1l);
        for (ProductCategory productCategory : list) {
            System.out.println(productCategory.toString());
        }
    }

    @Test
    public void testBatchSaveProductCategory() {
        List<ProductCategory> list = new ArrayList<>();
        ProductCategory a1 = new ProductCategory();
        a1.setShopId(7L);
        a1.setCreateTime(new Date());
        a1.setPriority(100);
        a1.setProductCategoryName("奶茶");

        ProductCategory a2 = new ProductCategory();
        a2.setShopId(7L);
        a2.setCreateTime(new Date());
        a2.setPriority(50);
        a2.setProductCategoryName("果汁");

        list.add(a1);
        list.add(a2);
        int count = dao.batchSaveProductCategory(list);
        Assert.assertEquals(count, 2);
    }
}
