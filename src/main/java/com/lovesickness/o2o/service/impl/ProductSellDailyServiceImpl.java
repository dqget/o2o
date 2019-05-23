package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.ProductSellDailyDao;
import com.lovesickness.o2o.entity.ProductSellDaily;
import com.lovesickness.o2o.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 懿
 */
@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {
    private static Logger logger = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);
    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Override
    public void dailyCalculate() {
        logger.info("Quartz Running!");
        productSellDailyDao.insertProductSellDaily();
        productSellDailyDao.insertDefaultProductSellDaily();
    }

    @Override
    public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime) {
        //空值判断
        if (productSellDailyCondition != null) {
            return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition, beginTime, endTime);
        } else {
            return null;
        }
    }

    @Override
    public void printfHello() {
        System.out.println("Hello World");
    }
}
