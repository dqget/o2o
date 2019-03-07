package com.lovesickness.o2o.dao;

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
public class PersonInfoDaoTest {
    @Autowired
    private PersonInfoDao personInfoDao;

    @Test
    public void testAqueryPersonInfoById() {
        PersonInfo personInfo = personInfoDao.queryPersonInfoById(2);
        System.out.println(personInfo);
    }

    @Test
    public void testBinsertPersonInfo() {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserType(1);
        personInfo.setName("小慧慧");
        personInfo.setGender("女");
        personInfo.setCreateTime(new Date());
        personInfo.setLastEditTime(new Date());
        personInfo.setEnableStatus(1);
        personInfo.setEmail("1197537545@qq.com");
        int effectedNum = personInfoDao.insertPersonInfo(personInfo);
        assertEquals(1, effectedNum);
    }
}
