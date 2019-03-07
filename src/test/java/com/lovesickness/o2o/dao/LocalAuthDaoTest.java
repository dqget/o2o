package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.LocalAuth;
import com.lovesickness.o2o.entity.PersonInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthDaoTest {
    @Autowired
    private LocalAuthDao localAuthDao;

    @Test
    public void testAInsertLocalAuth() {
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);
        localAuth.setPersonInfo(personInfo);
        localAuth.setPassword("123456");
        localAuth.setCreateTime(new Date());
        localAuth.setLastEditTime(new Date());
        localAuth.setUserName("testxiaoy");
        int effectedNum = localAuthDao.insertLocalAuth(localAuth);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testBqueryLocalByUserId() {
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
        assertEquals("刁强", localAuth.getPersonInfo().getName());
    }

    @Test
    public void testCqueryLocalByUserNameAndPwd() {
        LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd("xiaoy", "25q2y22l06q99569529l52q26qqs2bq6");
        System.out.println(localAuth);

    }

    @Test
    public void testDupdateLocalAuth() {
        int effectedNum = localAuthDao.updateLocalAuth(1L, "xiaoy", "123", "25q2y22l06q99569529l52q26qqs2bq6", new Date());
        assertEquals(1, effectedNum);
        System.out.println(localAuthDao.queryLocalByUserId(1L));

    }
}
