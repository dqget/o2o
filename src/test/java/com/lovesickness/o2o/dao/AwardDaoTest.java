package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Award;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AwardDaoTest {
    @Autowired
    private AwardDao awardDao;

    @Test
    public void testBInsertAward() {
        Award award = new Award();
        award.setCreateTime(new Date());
        award.setLastEditTime(new Date());
        award.setAwardName("测试奖品1");
        award.setAwardDesc("描述");
        award.setEnableStatus(1);
        award.setPoint(1);
        award.setPriority(20);
        award.setShopId(12L);
        int effectedNum = awardDao.insertAward(award);
        assertEquals(effectedNum, 1);
    }

    @Test
    public void testAQueryAwardList() {
        Award award = new Award();
        List<Award> awardList = awardDao.queryAwardList(award, 0, 3);
        awardList.forEach(System.out::println);
    }

    @Test
    public void testCQueryAwardCount() {
        Award award = new Award();
        int effectedNum = awardDao.queryAwardCount(award);
        assertEquals(effectedNum, 1);

    }

    @Test
    public void testDUpdateAward() {
        Award award = new Award();
        award.setShopId(12L);
        award.setAwardId(1L);
        award.setAwardDesc("修改描述");
        int effectedNum = awardDao.updateAward(award);
        assertEquals(effectedNum, 1);
    }
    @Test
    public void testEDeleteAward(){
        Award award = new Award();
        award.setShopId(12L);
        award.setAwardId(1L);
        int effectedNum = awardDao.deleteAward(award.getAwardId(),award.getShopId());
        assertEquals(effectedNum, 1);
    }
}
