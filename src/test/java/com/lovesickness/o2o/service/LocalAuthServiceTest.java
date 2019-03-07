package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.LocalAuthExecution;
import com.lovesickness.o2o.entity.LocalAuth;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.enums.LocalAuthStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthServiceTest {
    @Autowired
    private LocalAuthService localAuthService;

    @Test
    public void testABindLocalAuth() {
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);
        localAuth.setUserName("xiaoy");
        localAuth.setPassword("123456");
        localAuth.setPersonInfo(personInfo);
        LocalAuthExecution le = localAuthService.bindLocalAuth(localAuth);
        assertEquals(LocalAuthStateEnum.SUCCESS.getState(), le.getState());
        System.out.println(le.getLocalAuth());
    }

    @Test
    public void testBModifyLocalAuth() {
        LocalAuthExecution le = localAuthService.modifyLocalAuth(1L, "xiaoy", "123", "123456");
        assertEquals(LocalAuthStateEnum.SUCCESS.getState(), le.getState());

    }
}
