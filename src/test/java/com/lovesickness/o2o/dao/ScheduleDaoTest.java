package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.entity.Schedule;
import com.lovesickness.o2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScheduleDaoTest {
    @Autowired
    private ScheduleDao scheduleDao;

    @Test
    public void testAQuerySchedule() {
        Schedule schedule = scheduleDao.queryScheduleById(1L);
        System.out.println(schedule);
    }

    @Test
    public void testBInsertSchedule() {
        Schedule schedule = new Schedule();
        schedule.setAmountDay(30);
        schedule.setCreateTime(new Date());
        schedule.setDailyQuantity(11);
        schedule.setIsPay(1);
        schedule.setPayPrice("500");
        schedule.setPayTime(new Date());
        schedule.setProductPrice("501");
        schedule.setEnableStatus(1);
        schedule.setUpdateTime(new Date());
        Product product = new Product();
        product.setProductId(9L);
        schedule.setProduct(product);
        Shop shop = new Shop();
        shop.setShopId(14L);
        schedule.setShop(shop);
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(10L);
        schedule.setUser(personInfo);

        int effectedNum = scheduleDao.insertSchedule(schedule);
        assertEquals(effectedNum, 1);
//        System.out.println(schedule);
    }

    @Test
    public void testCUpdateSchedule() {
        Schedule schedule = scheduleDao.queryScheduleById(1L);
        schedule.setUpdateTime(new Date());
        schedule.setProductPrice("777");
        schedule.setPayPrice("111");
        schedule.setIsPay(0);
        schedule.setPayTime(new Date());
        schedule.setDailyQuantity(5);
        schedule.setAmountDay(180);
        int effectedNum = scheduleDao.updateSchedule(schedule);
        assertEquals(effectedNum, 1);

    }
}
