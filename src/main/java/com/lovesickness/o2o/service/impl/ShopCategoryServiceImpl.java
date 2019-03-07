package com.lovesickness.o2o.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovesickness.o2o.cache.JedisUtil;
import com.lovesickness.o2o.dao.ShopCategoryDao;
import com.lovesickness.o2o.entity.ShopCategory;
import com.lovesickness.o2o.service.ShopCategoryService;
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
public class ShopCategoryServiceImpl implements ShopCategoryService {
    private static Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);
    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Autowired
    private JedisUtil jedisUtil;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        String key = SHOPCATEGORYLISTKEY;
        List<ShopCategory> shopCategoryList;
        String shopCategoryJson;
        ObjectMapper mapper = new ObjectMapper();
        Jedis jedis = null;
        if (shopCategoryCondition == null) {
            //若shopCategoryCondition为空，则查询一级分类
            key += "_allfirstlevel";
        } else if (shopCategoryCondition.getParent() != null &&
                shopCategoryCondition.getParent().getShopCategoryId() != null) {
            //若parentId不为空，则查询该parentId下的所有二级标题
            key += "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
        } else {
            //查询所有二级分类
            key += "_allsecondlevel";
        }
        try {
            jedis = jedisUtil.getJedis();
            if (!jedis.exists(key)) {
                shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
                shopCategoryJson = mapper.writeValueAsString(shopCategoryList);
                jedis.set(key, shopCategoryJson);

            } else {
                shopCategoryJson = jedis.get(key);
                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
                shopCategoryList = mapper.readValue(shopCategoryJson, javaType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ShopCategoryServiceImpl getShopCategoryList error : " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (jedis != null) {
                jedisUtil.returnJedis(jedis);
            }
        }
        return shopCategoryList;
    }

}
