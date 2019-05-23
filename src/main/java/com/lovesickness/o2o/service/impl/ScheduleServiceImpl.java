package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.*;
import com.lovesickness.o2o.dto.ScheduleExecution;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.entity.Schedule;
import com.lovesickness.o2o.entity.ScheduleDistribution;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.enums.ScheduleStateEnum;
import com.lovesickness.o2o.exception.ScheduleOperationException;
import com.lovesickness.o2o.service.ScheduleService;
import com.lovesickness.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleDao scheduleDao;
    @Autowired
    private ScheduleDistributionDao scheduleDistributionDao;
    @Autowired
    private UserProductMapDao userProductMapDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ShopDao shopDao;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ScheduleExecution addSchedule(Schedule schedule) throws ScheduleOperationException {
        if (schedule != null && schedule.getShop() != null && schedule.getProduct() != null
                && schedule.getAmountDay() >= 1 && schedule.getDailyQuantity() >= 1) {
            schedule.setCreateTime(new Date());
            Product product =
                    productDao.queryProductByProductId(schedule.getProduct().getProductId());
            //判断商品ID是否合法
            if (product == null || product.getProductId() == null) {
                throw new ScheduleOperationException("商品不存在");
            }
            schedule.setProduct(product);
            //
            int promotionPrice = Integer.parseInt(product.getPromotionPrice());
            schedule.setProductPrice(product.getPromotionPrice());
            //添加预定记录需要付款的数目   总天数*每天送的商品数*商品单价
            int payPrice = schedule.getAmountDay() * schedule.getDailyQuantity() * promotionPrice;

            schedule.setPayPrice(String.valueOf(payPrice));
            //判断商铺信息是否合法
            Shop shop = shopDao.queryByShopId(schedule.getShop().getShopId());
            if (shop == null || shop.getShopId() == null) {
                throw new ScheduleOperationException("店铺不存在");
            }
            schedule.setShop(shop);
            //添加预定记录
            int effectedNum = scheduleDao.insertSchedule(schedule);
            if (effectedNum <= 0) {
                throw new ScheduleOperationException(ScheduleStateEnum.INNER_ERROR.getStateInfo());
            }
            //预定配送记录
            List<ScheduleDistribution> scheduleDistributions = new ArrayList<>();
            //用户购买记录
//            List<UserProductMap> userProductMaps = new ArrayList<>();

            //储存 默认的 收货人 收货人地址 收货人手机 起始日期(baseScheduleDistribution.getReceiptTime())
            ScheduleDistribution baseScheduleDistribution =
                    schedule.getScheduleDistributionList().get(0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(baseScheduleDistribution.getReceiptTime());

            //选择总天数
            int amountDay = schedule.getAmountDay();
            for (int i = 0; i < amountDay; i++) {
                //构建一条预定配送记录
                ScheduleDistribution scheduleDistribution = new ScheduleDistribution();
                scheduleDistribution.setScheduleId(schedule.getScheduleId());
                scheduleDistribution.setReceiveAddr(baseScheduleDistribution.getReceiveAddr());
                scheduleDistribution.setReceiveName(baseScheduleDistribution.getReceiveName());
                scheduleDistribution.setReceivePhone(baseScheduleDistribution.getReceivePhone());
                scheduleDistribution.setReceiptTime(calendar.getTime());
                //将日期加1天
                calendar.add(Calendar.DATE, 7);

                scheduleDistributions.add(scheduleDistribution);

            }
            //添加预定配送记录
            effectedNum = scheduleDistributionDao.batchInsertScheduleDistribution(scheduleDistributions);
            if (effectedNum != amountDay) {
                throw new ScheduleOperationException("添加预定配送记录失败");
            }

            return new ScheduleExecution(ScheduleStateEnum.SUCCESS);
        } else {
            return new ScheduleExecution(ScheduleStateEnum.EMPTY);

        }
    }

    @Override
    public ScheduleExecution getScheduleList(Schedule scheduleCondition, Integer pageIndex, Integer pageSize) {
        ScheduleExecution se;
        if (scheduleCondition != null && pageIndex != null && pageSize != null) {
            int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            se = new ScheduleExecution(ScheduleStateEnum.SUCCESS);
            se.setCount(scheduleDao.queryScheduleCount(scheduleCondition));
            se.setScheduleList(scheduleDao.queryScheduleList(scheduleCondition, rowIndex, pageSize));
        } else {
            se = new ScheduleExecution(ScheduleStateEnum.EMPTY);
        }
        return se;
    }

    @Override
    public Schedule getScheduleById(long scheduleId) {
        return scheduleDao.queryScheduleById(scheduleId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ScheduleExecution modifySchedule(Schedule schedule) throws ScheduleOperationException {
        if (schedule != null && schedule.getScheduleId() != null) {
            int effectedNum = scheduleDao.updateSchedule(schedule);
            if (effectedNum != 1) {
                throw new ScheduleOperationException("修改失败");
            }
            return new ScheduleExecution(ScheduleStateEnum.SUCCESS);
        } else {
            return new ScheduleExecution(ScheduleStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ScheduleExecution modifyScheduleDistributionByUser(ScheduleDistribution scheduleDistribution) throws ScheduleOperationException {

        if (scheduleDistribution != null && scheduleDistribution.getScheduleDistributionId() != null) {
            ScheduleDistribution oldDistribution = scheduleDistributionDao.queryScheduleDistributionById(scheduleDistribution.getScheduleDistributionId());
            if (oldDistribution.getIsReceipt() == 0) {
                //将用户输入的信息 填到oldDistribution里
                compactScheduleDistribution4modifyByUser(oldDistribution, scheduleDistribution);
                int effectedNum = scheduleDistributionDao.updateScheduleDistribution(oldDistribution);
                if (effectedNum != 1) {
                    throw new ScheduleOperationException("修改配送记录失败");
                }
                return new ScheduleExecution(ScheduleStateEnum.SUCCESS);
            } else {
                throw new ScheduleOperationException("不可以更改已配送的记录");
            }

        } else {
            return new ScheduleExecution(ScheduleStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ScheduleExecution modifyScheduleDistributionByShop(ScheduleDistribution scheduleDistribution) throws ScheduleOperationException {
        if (scheduleDistribution != null && scheduleDistribution.getScheduleDistributionId() != null) {
            scheduleDistribution.setUpdateTime(new Date());
            int effectedNum = scheduleDistributionDao.updateScheduleDistribution(scheduleDistribution);
            if (effectedNum != 1) {
                throw new ScheduleOperationException("修改配送记录失败");
            }
            return new ScheduleExecution(ScheduleStateEnum.SUCCESS);
        } else {
            return new ScheduleExecution(ScheduleStateEnum.EMPTY);
        }
    }

    @Override
    public ScheduleDistribution getScheduleDistributionById(long scheduleDistributionId) {
        return scheduleDistributionDao.queryScheduleDistributionById(scheduleDistributionId);
    }

    private void compactScheduleDistribution4modifyByUser(ScheduleDistribution oldDistribution, ScheduleDistribution scheduleDistribution) {
        oldDistribution.setReceiptTime(scheduleDistribution.getReceiptTime());
        oldDistribution.setReceiveAddr(scheduleDistribution.getReceiveAddr());
        oldDistribution.setReceivePhone(scheduleDistribution.getReceivePhone());
        oldDistribution.setReceiveName(scheduleDistribution.getReceiveName());
    }
}
