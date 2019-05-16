package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.EvaluationExecution;
import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.dto.ProductExecution;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.exception.ProductOperationException;

import java.util.List;

public interface ProductService {

    /**
     * 添加商品及图片信息
     *
     * @param product        2
     * @param thumbnail
     * @param productImgList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder thumbnail,
                                List<ImageHolder> productImgList) throws ProductOperationException;

    /**
     * 修改商品信息
     *
     * @param product
     * @param thumbnail
     * @param productImgList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
                                   List<ImageHolder> productImgList) throws ProductOperationException;

    /**
     * 根据productId查询商品详情
     *
     * @param productId
     * @return
     */
    Product getProductById(long productId);

    /**
     * 根据productCondition查询productList
     *
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

    EvaluationExecution getProductEvaluation(Product productCondition, Integer pageIndex, Integer pageSize);

    List<Product> getProductAveStar(long shopId);
}