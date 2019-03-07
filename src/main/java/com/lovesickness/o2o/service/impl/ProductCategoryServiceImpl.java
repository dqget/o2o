package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.ProductCategoryDao;
import com.lovesickness.o2o.dao.ProductDao;
import com.lovesickness.o2o.dao.ShopDao;
import com.lovesickness.o2o.dto.ProductCategoryExecution;
import com.lovesickness.o2o.entity.ProductCategory;
import com.lovesickness.o2o.enums.ProductCategoryStateEnum;
import com.lovesickness.o2o.exception.ProductCategoryOperationException;
import com.lovesickness.o2o.exception.ShopOperationException;
import com.lovesickness.o2o.service.ProductCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private static Logger logger = LoggerFactory.getLogger(ProductCategoryServiceImpl.class);
    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        List<ProductCategory> list;
        try {
            list = productCategoryDao.queryProductCategoryByShopId(shopId);
        } catch (Exception e) {
            throw new ShopOperationException("查询商品分类列表错误：" + e);
        }
        if (list == null || list.size() == 0) {
            list = new ArrayList<>();
        }
        logger.info("Service层返回ProductCategoryList.size=" + list.size());
        return list;
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
        if (productCategoryList == null || productCategoryList.size() == 0) {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        } else {
            try {
                for (ProductCategory productCategory : productCategoryList) {
                    productCategory.setCreateTime(new Date());
                }
                int effectedNum = productCategoryDao.batchSaveProductCategory(productCategoryList);
                if (effectedNum <= 0) {
                    throw new ProductCategoryOperationException("店铺类别创建失败");
                } else {
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            } catch (Exception e) {
                throw new ProductCategoryOperationException("batchAddProductCategory error:" + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException {
        logger.info("Server层: deleteProductCategory productCategoryId=" + productCategoryId + " shopId = " + shopId);
        //此类别下的商品里的列别ID置为空
        try {
            int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
            if (effectedNum < 0) {
                throw new ProductCategoryOperationException("商品类别更新失败");
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("deleteProductCategory error: " + e.getMessage());
        }
        //
        try {
            int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
            if (effectedNum <= 0) {
                throw new ProductCategoryOperationException("商品类别删除失败");
            } else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException("deleteProductCategory error " + e.getMessage());
        }
    }

}
