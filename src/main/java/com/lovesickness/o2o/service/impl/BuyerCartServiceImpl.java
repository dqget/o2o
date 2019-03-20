package com.lovesickness.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovesickness.o2o.cache.JedisUtil;
import com.lovesickness.o2o.entity.BuyerCartItem;
import com.lovesickness.o2o.service.BuyerCartService;
import com.lovesickness.o2o.util.BuyerCartItemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BuyerCartServiceImpl implements BuyerCartService {
    private static Logger logger = LoggerFactory.getLogger(BuyerCartServiceImpl.class);
    @Autowired
    private JedisUtil jedisUtil;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean addItem(Long userId, BuyerCartItem newCartItem) {
        //用户id不能为空  商品id不能为空  添加商品的数量不能为空或0
        if (userId == null || !BuyerCartItemUtil.isRightItem(newCartItem)) {
            return false;
        }
        String key = "cart_" + userId;
        Jedis jedis = jedisUtil.getJedis();
        List<BuyerCartItem> buyerCart;
        if (jedis.exists(key)) {
            buyerCart = getBuyerCart(userId);
            //是否已经添加过该商品
            boolean isHave = buyerCart.contains(newCartItem);
            if (isHave) {
                BuyerCartItem oldItem = buyerCart
                        .stream()
                        .filter(newCartItem::equals)
                        .collect(Collectors.toList()).get(0);
                newCartItem.setAmount(oldItem.getAmount() + newCartItem.getAmount());
                buyerCart.remove(oldItem);
                buyerCart.add(newCartItem);
            } else {
                buyerCart.add(newCartItem);
            }
        } else {
            buyerCart = new ArrayList<>();
            buyerCart.add(newCartItem);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            jedis.set(key, mapper.writeValueAsString(buyerCart));
            jedis.expire(key, 60 * 60 * 24);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("BuyerCartServiceImpl addItem error" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            jedisUtil.returnJedis(jedis);
        }
        return true;
    }

    @Override
    public Integer getProductAmount(Long userId) {
        List<BuyerCartItem> buyerCart = getBuyerCart(userId);
        if (buyerCart == null || buyerCart.size() == 0) {
            return 0;
        }
        return buyerCart
                .stream()
                .mapToInt(BuyerCartItem::getAmount)
                .sum();
    }

    @Override
    public Integer getProductPrice(Long userId) {
        List<BuyerCartItem> buyerCart = getBuyerCart(userId);
        if (buyerCart == null || buyerCart.size() == 0) {
            return 0;
        }
        return buyerCart
                .stream()
                //商品折价 * 添加商品的数量
                .mapToInt(item -> Integer.valueOf(item.getProduct().getPromotionPrice()) * item.getAmount())
                .sum();
    }

    @Override
    public Integer getProductNormalPrice(Long userId) {
        List<BuyerCartItem> buyerCart = getBuyerCart(userId);
        if (buyerCart == null || buyerCart.size() == 0) {
            return 0;
        }
        return buyerCart
                .stream()
                //商品原价 * 该商品的数量
                .mapToInt(item -> Integer.valueOf(item.getProduct().getNormalPrice()) * item.getAmount())
                .sum();
    }

    @Override
    public List<BuyerCartItem> getBuyerCart(Long userId) {
        String key = "cart_" + userId;
        Jedis jedis = jedisUtil.getJedis();
        ObjectMapper mapper = new ObjectMapper();
        List<BuyerCartItem> buyerCart = null;
        try {
            if (jedis.exists(key)) {
                JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, BuyerCartItem.class);
                buyerCart = mapper.readValue(jedis.get(key), javaType);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            jedisUtil.returnJedis(jedis);
        }
        return buyerCart;
    }
}
