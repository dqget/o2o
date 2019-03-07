package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.ProductCategoryExecution;
import com.lovesickness.o2o.entity.ProductCategory;
import com.lovesickness.o2o.exception.ProductCategoryOperationException;

import java.util.List;

public interface ProductCategoryService {
    /***
     * 根据shopid查询商品分类列表
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategoryList(long shopId);

    /***
     *
     * @param productCategoryList
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;

    /***
     * 将此类别下的商品里的列别ID置为空，再删除掉改商品类别
     * @param productCategoryId
     * @param shopId
     * @return
     * @throws ProductCategoryOperationException
     */
    ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException;
}
