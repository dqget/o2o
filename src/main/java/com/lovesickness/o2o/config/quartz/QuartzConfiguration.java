package com.lovesickness.o2o.config.quartz;

import com.lovesickness.o2o.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Objects;

/**
 * @author 懿
 */
@Configuration
public class QuartzConfiguration {
    private static Logger logger = LoggerFactory.getLogger(QuartzConfiguration.class);

    @Autowired
    private ProductSellDailyService productSellDailyService;
    @Autowired
    private MethodInvokingJobDetailFactoryBean jobDetailFactory;
    @Autowired
    private CronTriggerFactoryBean productSellDailyTriggerFactory;

    @Bean(name = "jobDetailFactory")
    public MethodInvokingJobDetailFactoryBean createJobDetail() {
        //new出一个jobDetailFactory对象，此工厂主要用来制作一个jobDetail，即制作一个任务
        //由于我们所作的定时任务根本上讲其实就是执行一个方法，所以用这个工厂比较方便
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        //设置jobDetail的名字
        jobDetailFactoryBean.setName("product_sell_daily_job");
        //设置jobDetail的组
        jobDetailFactoryBean.setGroup("job_product_sell_daily_group");
        //对于相同的jobDetail，当指定多个Trigger时，很可能第一个job完成之前，第二个job就开始了
        //滴定concurrent设为false，多个job不会并发运行，第二个job将不会再第一个job完成之前开始
        jobDetailFactoryBean.setConcurrent(false);
        //指定运行任务的类
        jobDetailFactoryBean.setTargetObject(productSellDailyService);
        //指定运行任务的方法 根据反射
        jobDetailFactoryBean.setTargetMethod("dailyCalculate");
//        jobDetailFactoryBean.setTargetMethod("printfHello");

        return jobDetailFactoryBean;
    }

    /**
     *
     * @return Cron表达式触发器
     */
    @Bean(name = "productSellDailyTriggerFactory")
    public CronTriggerFactoryBean createProductSellDailyTrigger() {
        //创建TriggerFactory实例，用来创建trigger
        CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
        //设置triggerFactory的名字
        triggerFactory.setName("product_sell_daily_trigger");
        //设置triggerFactory的组名
        triggerFactory.setGroup("job_product_sell_daily_group");
        //绑定jobDetail
        triggerFactory.setJobDetail(Objects.requireNonNull(jobDetailFactory.getObject()));
        //设定cron表达式
        triggerFactory.setCronExpression("0 0 0 * * ? ");
//        triggerFactory.setCronExpression("0/10 * * * * ? ");
        return triggerFactory;

    }

    /**
     * 创建调度工程
     */
    @Bean("schedulerFactory")
    public SchedulerFactoryBean createSchedulerFactory() {
        logger.info("创建任务调度工厂");
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setTriggers(productSellDailyTriggerFactory.getObject());
        return schedulerFactoryBean;
    }
}
