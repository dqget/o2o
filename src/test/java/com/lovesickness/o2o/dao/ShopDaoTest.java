package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Area;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopDaoTest {
    @Autowired
    private ShopDao shopDao;

    @Test
    @Ignore
    public void testInsertShop() {
        Shop shop = new Shop();
        Area area = new Area();
        PersonInfo owner = new PersonInfo();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2L);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试店铺5");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        int effectedNum = shopDao.insertShop(shop);
        assertEquals(1, effectedNum);
    }

    @Test
    @Ignore
    public void testUpdateShop() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setEnableStatus(2);
        shop.setPhone("1786833018");
        shop.setLastEditTime(new Date());
        shop.setAdvice("审核通过");
        int effectedNum = shopDao.updateShop(shop);
        assertEquals(1, effectedNum);
    }

    @Test
    @Ignore
    public void testQueryShop() {
        Shop shop = shopDao.queryByShopId(7);
        System.out.println(shop.toString());
    }

    @Test
    public void testQuesyShopListAndCount() {
        Shop shop = new Shop();
        ShopCategory shopCategory1 = new ShopCategory();
        ShopCategory parentCateory = new ShopCategory();
        parentCateory.setShopCategoryId(1L);
        shopCategory1.setParent(parentCateory);
        shop.setShopCategory(shopCategory1);
//		PersonInfo personInfo = new PersonInfo();
//		personInfo.setUserId(1L);
//		ShopCategory shopCategory = new ShopCategory();
//		shopCategory.setShopCategoryId(2L);
//		Area area=new Area();
//		area.setAreaId(2L);

//		shop.setOwner(personInfo);
//		//shop.setShopCategory(shopCategory);
//		shop.setArea(area);
        List<Shop> queryShopList = shopDao.queryShopList(shop, 0, 999);
        int count = shopDao.queryShopCount(shop);

        System.out.println("list.size:" + queryShopList.size());
        System.out.println("count:" + count);
    }
}
