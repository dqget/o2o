package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.junit.Assert.assertEquals;

@Repository
public class ShopCategoryDaoTest {
    @Autowired
    ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryShopCategory() {

        ShopCategory shopCategory1 = new ShopCategory();
        ShopCategory parentCateory = new ShopCategory();
        parentCateory.setShopCategoryId(2L);
        shopCategory1.setParent(parentCateory);
//		ShopCategory shopCategory2=new ShopCategory();
//		shopCategory2.setParent(shopCategory1);

        List<ShopCategory> list = shopCategoryDao.queryShopCategory(null);


        assertEquals(1, list.size());
        System.out.println(list.get(0));
    }
}
