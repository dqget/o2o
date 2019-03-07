package com.lovesickness.o2o.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author 懿
 */
public class JedisPoolWriper {
    /**
     * 连接池对象
     */
    private JedisPool jedisPool;

    public JedisPoolWriper(final JedisPoolConfig poolConfig, final String host, final int port, String password) {
        try {
            jedisPool = new JedisPool(poolConfig, host, port, 2000, password);
            System.out.println("JedisPoolWriper password " + password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Redis连接池对象
     *
     * @return
     */
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    /**
     * 注入Redis连接池对象
     */
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
