package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.ProductSellDaily;
import com.lovesickness.o2o.entity.Shop;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductSellDailyDaoTest {
    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Test
    public void testAInsertProductSellDaily() {
        int effectedNum = productSellDailyDao.insertProductSellDaily();
        Assert.assertEquals(2, effectedNum);
    }

    @Test
    public void testBQueryProductSellDailyList() {
        ProductSellDaily productSellDaily = new ProductSellDaily();
        Shop shop = new Shop();
        shop.setShopId(12L);
        productSellDaily.setShop(shop);
        Date date = new Date();
        String str = "2019-2-1";
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println(date);
        List<ProductSellDaily> productSellDailyList =
                null;
        try {
            productSellDailyList = productSellDailyDao.queryProductSellDailyList(productSellDaily, format1.parse(str),
            new Date());
            productSellDailyList.forEach(System.out::println);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testCInsertDefaultProductSellDaily() {
        int effectedNum = productSellDailyDao.insertDefaultProductSellDaily();
        Assert.assertEquals(1, effectedNum);
    }
}
