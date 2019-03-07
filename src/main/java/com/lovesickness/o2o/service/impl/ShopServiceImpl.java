package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.ShopDao;
import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.dto.ShopExecution;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.enums.ShopStateEnum;
import com.lovesickness.o2o.exception.ShopOperationException;
import com.lovesickness.o2o.service.ShopService;
import com.lovesickness.o2o.util.ImageUtile;
import com.lovesickness.o2o.util.PageCalculator;
import com.lovesickness.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author 懿
 */
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ShopExecution addShop(Shop shop, ImageHolder image) throws ShopOperationException {
        //空值判断
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            //给店铺信息赋初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺
            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum <= 0)
                throw new ShopOperationException("店铺创建失败");
            else {
                if (image.getImage() != null) {
                    //存储图片
                    try {
                        addShopImg(shop, image);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error:" + e.getMessage());
                    }
                    //更新店铺的图片地址
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error" + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    /***
     * 将图片保存在本地，更改shop里的ShopImg
     * @param shop
     * @param image
     * @throws IOException
     */
    private void addShopImg(Shop shop, ImageHolder image) throws IOException {
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());

        String shopImgAddr = ImageUtile.generateThumbnails(image, dest);
        shop.setShopImg(shopImgAddr);
    }

    @Override
    public Shop queryShopById(long shopId) {
        Shop shop = shopDao.queryByShopId(shopId);
        return shop;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ShopExecution modifyShop(Shop shop, ImageHolder image)
            throws ShopOperationException {
        try {
            if (shop == null || shop.getShopId() == null) {
                return new ShopExecution(ShopStateEnum.NULL_SHOP);
            } else {
                //1.判断是否需要处理图片
                if (image != null && image.getImage() != null && image.getImageName() != null && !"".equals(image.getImageName())) {
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopId() != null) {
                        ImageUtile.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop, image);
                }
                //2.更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if (effectedNum <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ShopOperationException("modifyShop error:" + e.getMessage());
        }
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList != null) {
            se.setShopList(shopList);
            se.setCount(count);
        } else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

} 
