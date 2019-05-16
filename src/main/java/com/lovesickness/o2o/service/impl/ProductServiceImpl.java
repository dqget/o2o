package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.EvaluationDao;
import com.lovesickness.o2o.dao.ProductDao;
import com.lovesickness.o2o.dao.ProductImgDao;
import com.lovesickness.o2o.dto.EvaluationExecution;
import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.dto.ProductExecution;
import com.lovesickness.o2o.entity.Evaluation;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.entity.ProductImg;
import com.lovesickness.o2o.enums.EvaluationStateEnum;
import com.lovesickness.o2o.enums.ProductStateEnum;
import com.lovesickness.o2o.exception.ProductOperationException;
import com.lovesickness.o2o.service.ProductService;
import com.lovesickness.o2o.util.ImageUtile;
import com.lovesickness.o2o.util.PageCalculator;
import com.lovesickness.o2o.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 懿
 */
@Service
public class ProductServiceImpl implements ProductService {
    private static Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;
    @Autowired
    private EvaluationDao evaluationDao;

    /**
     * 1.处理product缩略图
     * 2.product写入数据库
     * 3.结合productId批量处理商品详情图
     * 4.productImgList写入数据库
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);
            if (thumbnail != null) {
                addThumbnail(product, thumbnail);
            }

            try {
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum != 1) {
                    throw new ProductOperationException("创建商品失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("addProduct error : " + e.toString());
            }
            //商品详情图不为空，添加
            if (productImgList != null && productImgList.size() > 0) {
                addProductImgList(product, productImgList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            //product为空
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            if (thumbnail != null) {
                Product tempProduct = productDao.queryProductByProductId(product.getProductId());
                ImageUtile.deleteFileOrPath(tempProduct.getImgAddr());
                addThumbnail(product, thumbnail);
            }
            //商品详情图不为空，删除、添加
            if (productImgList != null && productImgList.size() > 0) {
                deleteProductImgList(product.getProductId());
                addProductImgList(product, productImgList);
            }
            try {
                product.setLastEditTime(new Date());
                int effectedNum = productDao.updateProduct(product);
                if (effectedNum != 1) {
                    throw new ProductOperationException("修改商品失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ProductOperationException("updateProduct error : " + e.toString());
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            //product为空
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductByProductId(productId);
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        ProductExecution pe = new ProductExecution();
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        pe.setProductList(productDao.queryProductList(productCondition, rowIndex, pageSize));
        pe.setCount(productDao.queryProductCount(productCondition));
        return pe;
    }

    @Override
    public EvaluationExecution getProductEvaluation(Product productCondition, Integer pageIndex, Integer pageSize) {
        EvaluationExecution ee;
        if (productCondition != null && pageIndex != null && pageSize != null) {
            int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            Evaluation evaluation = new Evaluation();
            evaluation.setProduct(productCondition);
            ee = new EvaluationExecution(EvaluationStateEnum.SUCCESS);
            ee.setEvaluationList(evaluationDao.queryEvaluation(evaluation, rowIndex, pageSize));
            ee.setCount(evaluationDao.queryEvaluationCount(evaluation));
        } else {
            ee = new EvaluationExecution(EvaluationStateEnum.EMPTY);
        }
        return ee;
    }

    @Override
    public List<Product> getProductAveStar(long shopId) {
        return productDao.queryProductAveStar(shopId);
    }

    /**
     * 批量添加商品详情图
     *
     * @param product
     * @param productImgHolderList
     */
    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new ArrayList<>();
        for (ImageHolder image : productImgHolderList) {
            String imgAddr = ImageUtile.generateNomalImg(image, dest);
            ProductImg productImg = new ProductImg();
            productImg.setProductId(product.getProductId());
            productImg.setImgAddr(imgAddr);
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
        if (productImgList.size() > 0) {
            try {
                int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("创建商品详情图片失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("addProductImgList error : " + e.toString());
            }
        }
    }

    /***
     * 添加缩略图
     * @param product
     * @param thumbnail
     */
    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtile.generateThumbnails(thumbnail, dest);
        product.setImgAddr(thumbnailAddr);
    }

    private void deleteProductImgList(long productId) {
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        for (ProductImg productImg : productImgList) {
            ImageUtile.deleteFileOrPath(productImg.getImgAddr());
        }
        productImgDao.deleteProductImgByProductId(productId);
    }
}
