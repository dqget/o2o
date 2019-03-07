package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.entity.ProductCategory;
import com.lovesickness.o2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductTest {
    @Autowired
    private ProductDao productDao;

    @Test
    public void testAInsertProduct() {
        Product product = new Product();
        product.setCreateTime(new Date());
        product.setEnableStatus(1);
        product.setImgAddr("测试图片地址");
        product.setLastEditTime(new Date());
        product.setNormalPrice("100");
        product.setPromotionPrice("90");
        product.setPriority(100);
        product.setProductName("红豆奶茶");
        product.setProductDesc("奶茶描述");
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(4L);
        product.setProductCategory(productCategory);
        Shop shop = new Shop();
        shop.setShopId(7L);
        product.setShop(shop);
        int effectNum = productDao.insertProduct(product);
        assertEquals(1, effectNum);
    }

    @Test
    public void testBUpdateProduct() {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(7L);
        product.setShop(shop);
        product.setProductId(3L);
        product.setProductDesc("苹果汁描述");
        int effectedNum = productDao.updateProduct(product);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testCqueryProductByProductId() {
        Product product = new Product();
        product = productDao.queryProductByProductId(2L);
        System.out.println(product);
    }

    @Test
    public void testDqueryProductList() {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(7L);
        product.setShop(shop);
        List<Product> productList = productDao.queryProductList(product, 1, 3);
        int num = productDao.queryProductCount(product);
        System.out.println(num);
        System.out.println(productList);
    }

    @Test
    public void testEupdateProductCategoryToNull() {
        int effectedNum = productDao.updateProductCategoryToNull(5L);
        System.out.println(effectedNum);
    }
}
