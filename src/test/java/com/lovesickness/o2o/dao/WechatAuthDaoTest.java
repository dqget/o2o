package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.WechatAuth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthDaoTest {
    @Autowired
    private WechatAuthDao wechatAuthDao;

    @Test
    public void testAqueryWechatInfoByOpenId() {
        WechatAuth wechatAuth = wechatAuthDao.queryWechatInfoByOpenId("xiaoy");
        System.out.println(wechatAuth);
    }

    @Test
    public void testBAinsertWechatAuth() {
        WechatAuth wechatAuth = new WechatAuth();
        wechatAuth.setOpenId("xiaoy");
        wechatAuth.setCreateTime(new Date());
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);
        wechatAuth.setPersonInfo(personInfo);
        int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
        assertEquals(1, effectedNum);
    }
}
