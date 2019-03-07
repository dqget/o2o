package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.cache.JedisUtil;
import com.lovesickness.o2o.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author 懿
 */
@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private JedisUtil jedisUtil;

    @Override
    public void removeFromCache(String keyPrefix) {
        JedisUtil.Keys jedisKeys = jedisUtil.new Keys();

        Set<String> keySet = jedisKeys.keys(keyPrefix + "*");
        keySet.forEach(jedisKeys::del);

    }
}
