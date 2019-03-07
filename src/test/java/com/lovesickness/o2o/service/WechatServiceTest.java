package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.WechatAuthExecution;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.WechatAuth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatServiceTest {
    @Autowired
    private WechatAuthService wechatAuthService;

    @Test
    public void testAregister() {
        WechatAuth wechatAuth = new WechatAuth();
        wechatAuth.setCreateTime(new Date());
        wechatAuth.setOpenId("xiaoyy");
        PersonInfo personInfo = new PersonInfo();
        personInfo.setEmail("472576049@qq.com");
        personInfo.setGender("男");
        personInfo.setUserType(1);
        personInfo.setName("测试register");
        wechatAuth.setPersonInfo(personInfo);
        WechatAuthExecution we = wechatAuthService.register(wechatAuth);
        System.out.println(we.getStateInfo());
        System.out.println(we.getWechatAuth().getPersonInfo());
    }
}
