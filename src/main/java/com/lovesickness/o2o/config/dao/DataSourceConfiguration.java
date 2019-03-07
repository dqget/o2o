package com.lovesickness.o2o.config.dao;

import com.lovesickness.o2o.util.DESUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

/***
 * 配置dataSource到ioc容器
 * @author 懿
 */
@Configuration
@MapperScan("com.lovesickness.o2o.dao")
public class DataSourceConfiguration {
    @Value("${jdbc.driver}")
    private String jdbcDriver;
    @Value("${jdbc.master.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;


    /**
     * 生成与spring-da.xml对应的bean dataSource
     */
    @Bean(name = "dataSource")
    public ComboPooledDataSource createDataSource() throws PropertyVetoException {
        //生成dataSource实例
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        //设置配置信息
        //驱动
        dataSource.setDriverClass(jdbcDriver);
        //数据库连接Url
        dataSource.setJdbcUrl(jdbcUrl);
        //用户名
        dataSource.setUser(DESUtils.getDecryptString(jdbcUsername));
        //密码
        dataSource.setPassword(DESUtils.getDecryptString(jdbcPassword));
        //配置c3p0连接池的私有属性
        //连接池最大线程数
        dataSource.setMaxPoolSize(30);
        //连接池最小线程数
        dataSource.setMinPoolSize(10);
        //关闭连接后不自动commit
        dataSource.setAutoCommitOnClose(false);
        //连接超时时间
        dataSource.setCheckoutTimeout(10000);
        //连接失败尝试次数
        dataSource.setAcquireRetryAttempts(2);
        dataSource.setTestConnectionOnCheckin(false);
        dataSource.setTestConnectionOnCheckout(true);
        dataSource.setBreakAfterAcquireFailure(false);
        return dataSource;
    }
}
