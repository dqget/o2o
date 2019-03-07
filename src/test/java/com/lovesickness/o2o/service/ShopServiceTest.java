package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.dto.ShopExecution;
import com.lovesickness.o2o.entity.Area;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.ShopCategory;
import com.lovesickness.o2o.enums.ShopStateEnum;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testGetShopList() {
        Shop shop = new Shop();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(22L);
        Area area = new Area();
        area.setAreaId(4L);
        shop.setOwner(personInfo);
        shop.setArea(area);

        ShopExecution shopList = shopService.getShopList(shop, 1, 1);

        System.out.println("list.size:" + shopList.getShopList().size());
        System.out.println("count:" + shopList.getCount());
    }

    @Test
    @Ignore
    public void testAddShop() throws FileNotFoundException {
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
        shop.setShopName("测试店铺4");
        shop.setShopDesc("test4");
        shop.setShopAddr("test4");
        shop.setPhone("test4");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");

        File shopImg = new File("C:/Users/懿/Pictures/Feedback/{0E0AF7DD-E5BD-49D6-BA7A-EE5953A40D21}/Capture001.png");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder image = new ImageHolder(is, shopImg.getName());
        ShopExecution se = shopService.addShop(shop, image);
        assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
    }
}
