package com.lovesickness.o2o.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovesickness.o2o.cache.JedisUtil;
import com.lovesickness.o2o.dao.HeadLineDao;
import com.lovesickness.o2o.entity.HeadLine;
import com.lovesickness.o2o.service.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {
    private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);
    @Autowired
    private HeadLineDao headLineDao;
    @Autowired
    private JedisUtil jedisUtil;

    @Override
    @Transactional
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {

        ObjectMapper mapper = new ObjectMapper();
        List<HeadLine> headLineList;
        String headLineJson;
        String key = HEADLINEKETY;
        Jedis jedis = null;
        if (headLineCondition != null && headLineCondition.getEnableStatus() != null) {
            key = HEADLINEKETY + "_" + headLineCondition.getEnableStatus();
        }
        try {
            jedis = jedisUtil.getJedis();
            if (!jedis.exists(key)) {
                headLineList = headLineDao.queryHeadLine(headLineCondition);
                headLineJson = mapper.writeValueAsString(headLineList);
                jedis.set(key, headLineJson);
                jedis.expire(key, 60 * 60 * 24);
            } else {
                headLineJson = jedis.get(key);
                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
                headLineList = mapper.readValue(headLineJson, javaType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("HeadLineServiceImpl getHeadLineList error : " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            jedisUtil.returnJedis(jedis);
        }
        return headLineList;
    }
}
