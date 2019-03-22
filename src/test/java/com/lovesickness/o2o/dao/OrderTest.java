package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.util.IdGenerator;
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
public class OrderTest {
    @Autowired
    private OrderDao orderDao;


    @Test
    public void testAInsertOrder() {
        Order order = new Order();
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        order.setUser(user);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setOrderNumber(IdGenerator.INSTANCE.nextId());
        order.setPayPrice("444");
        order.setReceiveName("xiaoming");
        order.setReceiveAddr("山东济南历城区");
        order.setReceivePhone("17865313018");
        int effectedNum = orderDao.insertOrder(order);
        assertEquals(effectedNum, 1);
        System.out.println(order);
    }

    @Test
    public void testBQueryOrderByNo() {
        Order order = orderDao.queryOrderByNo("20190317140336633221184");
        System.out.println("OrderTest.testBQueryOrderByNo");
        System.out.println("order = " + order);
    }

    @Test
    public void testCQueryOrderList() {
        Order orderCondition = new Order();
        orderCondition.setIsPay(1);
        List<Order> orders = orderDao.queryOrderList(orderCondition, 0, 999);
        int effectedNum = orderDao.queryOrderCount(orderCondition);
        System.out.println(orders);
        System.out.println(effectedNum);
    }
}
