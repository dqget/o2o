package com.lovesickness.o2o.config.redis;

import com.lovesickness.o2o.cache.JedisPoolWriper;
import com.lovesickness.o2o.cache.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author 懿
 */
@Configuration
public class RedisConfiguration {
    public static final Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);
    @Value("${redis.hostname}")
    private String hostname;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.pool.maxActive}")
    private int maxTotal;
    @Value("${redis.pool.maxIdle}")
    private int maxIdle;
    @Value("${redis.pool.maxWait}")
    private long maxWaitMillis;
    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;
    @Autowired
    private JedisPoolWriper jedisWritePool;

    /**
     * Redis连接池配置
     *
     * @return
     */
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //最大连接数 控制一个pool可配置多少个jedis实例
        jedisPoolConfig.setMaxTotal(maxTotal);
        //最大空闲数
        jedisPoolConfig.setMaxIdle(maxIdle);
        //最大等待时间 当没有可用连接时，连接池等待连接被归还的最大时间（以毫秒计数），超过时间则抛出异常
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        //在获取连接的时候检查有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);

        return jedisPoolConfig;
    }

    /**
     * Redis连接池
     *
     * @return
     */
    @Bean(name = "jedisWritePool")
    public JedisPoolWriper createJedisWritePool() {
        logger.info("createJedisWritePool执行");
        return new JedisPoolWriper(jedisPoolConfig, hostname, port, password);
    }

    /**
     * Redis工具类
     *
     * @return
     */
    @Bean(name = "jedisUtil")
    public JedisUtil cerateJedisUtil() {
        logger.info("cerateJedisUtil执行");
        JedisUtil jedisUtils = new JedisUtil();
        jedisUtils.setJedisPool(jedisWritePool);
        return jedisUtils;
    }
}
