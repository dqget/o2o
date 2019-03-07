package com.lovesickness.o2o.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovesickness.o2o.cache.JedisUtil;
import com.lovesickness.o2o.dao.AreaDao;
import com.lovesickness.o2o.entity.Area;
import com.lovesickness.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 懿
 */
@Service
public class AreaServiceImpl implements AreaService {
    private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private JedisUtil jedisUtil;

    @Override
    @Transactional
    public List<Area> getAreaList() {
        ObjectMapper mapper = new ObjectMapper();
        String areaListJson;
        List<Area> areaList;
        Jedis jedis = null;
        try {
            jedis = jedisUtil.getJedis();
            if (!jedis.exists(AREAKEY)) {
                areaList = areaDao.queryArea();
                areaListJson = mapper.writeValueAsString(areaList);
                jedis.set(AREAKEY, areaListJson);
                //存储5天时间
                jedis.expire(AREAKEY, 60 * 60 * 24 * 5);
            } else {
                areaListJson = jedis.get(AREAKEY);
                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
                areaList = mapper.readValue(areaListJson, javaType);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("AreaServiceImpl getAreaList error : " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (jedis != null) {
                jedisUtil.returnJedis(jedis);
            }
        }
        return areaList;
    }
}
