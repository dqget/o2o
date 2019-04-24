package com.lovesickness.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovesickness.o2o.cache.JedisUtil;
import com.lovesickness.o2o.entity.BuyerCartItem;
import com.lovesickness.o2o.entity.Product;
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


@Service
public class BuyerCartServiceImpl implements BuyerCartService {
    private static Logger logger = LoggerFactory.getLogger(BuyerCartServiceImpl.class);
    @Autowired
    private JedisUtil jedisUtil;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateItem(Long userId, List<BuyerCartItem> newCartItems) {
        //用户id不能为空  商品id不能为空  添加商品的数量不能为空或0
        if (userId == null || !BuyerCartItemUtil.isRightItem(newCartItems)) {
            return false;
        }
        String key = "cart_" + userId;
        Jedis jedis = jedisUtil.getJedis();
        ObjectMapper mapper = new ObjectMapper();
        List<BuyerCartItem> buyerCart;
        if (jedis.exists(key)) {
            //1.先查询购物车列表
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, BuyerCartItem.class);
            try {
                buyerCart = mapper.readValue(jedis.get(key), javaType);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            if (buyerCart == null) {
                buyerCart = new ArrayList<>();
            }

            //2.判断购物车中是否含有想要修改的产品
            for (BuyerCartItem newCartItem : newCartItems) {
                //是否已经添加过该商品
                //如果存在，更改产品数量
                if (buyerCart.contains(newCartItem)) {
                    BuyerCartItem oldItem = buyerCart
                            .stream()
                            .filter(buyerCartItem -> buyerCartItem.equals(newCartItem))
                            .findFirst()
                            .get();
                    int amount = oldItem.getAmount() + newCartItem.getAmount();
                    if (amount < 0) {
                        throw new RuntimeException("产品数量修改失败");
                    } else if (amount == 0) {
                        buyerCart.remove(oldItem);
                    } else {
                        newCartItem.setAmount(amount);
                        buyerCart.remove(oldItem);
                        buyerCart.add(newCartItem);
                    }
                } else {
                    if (newCartItem.getAmount() <= 0) {
                        throw new RuntimeException("产品数量修改失败");
                    }
                    buyerCart.add(newCartItem);
                }
            }
        } else {
            if (BuyerCartItemUtil.isRightAddItem(newCartItems)) {
                buyerCart = newCartItems;
            } else {
                throw new RuntimeException("购物车内商品为空");
            }
        }
        //添加到redis 储存时间为1天
        try {
            jedis.set(key, mapper.writeValueAsString(buyerCart));
            jedis.expire(key, 60 * 60 * 24);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("BuyerCartServiceImpl updateItem error" + e.getMessage());
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

    @Override
    public BuyerCartItem getBuyerCartByProductId(Long userId, Long productId) {
        List<BuyerCartItem> buyerCart = getBuyerCart(userId);
        Product product = new Product();
        product.setProductId(productId);
        BuyerCartItem itemCondition = new BuyerCartItem(product, 0);
        //如果在购物车中找到了 就返回购物车中该产品的信息，如果没有找到，就说明购物车中该产品的数目为0
        return buyerCart
                .stream()
                .filter(buyerCartItem -> buyerCartItem.equals(itemCondition))
                .findFirst()
                .orElse(itemCondition);
    }
}
