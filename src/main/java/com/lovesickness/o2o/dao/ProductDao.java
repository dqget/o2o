package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao {

    /***
     * 添加商品
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /***
     * 修改商品
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     * =
     * 根据productId查询指定商品
     *
     * @param productId
     * @return
     */
    Product queryProductByProductId(Long productId);

    /**
     * 查询商品列表并分页，可输入的条件有：商品名（模糊），商品状态，店铺Id,商品类别ID
     *
     * @param productCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Product> queryProductList(
            @Param("productCondition") Product productCondition,
            @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 查询对应的商品总数
     *
     * @param productCondition
     * @return
     */
    int queryProductCount(@Param("productCondition") Product productCondition);

    int updateProductCategoryToNull(long productCategoryId);
}
