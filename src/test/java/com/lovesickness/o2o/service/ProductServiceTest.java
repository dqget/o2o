package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.dto.ProductExecution;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.entity.ProductCategory;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.enums.ProductStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService server;

    @Test
    public void TestAddProduct() throws FileNotFoundException {

        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(7L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(5L);
        product.setShop(shop);
        product.setProductCategory(productCategory);
        product.setProductName("橙汁");
        product.setProductDesc("测试商品");
        product.setPriority(100);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        product.setEnableStatus(1);

        File file1 = new File("E:/cww/Carousel1.jpg");
        InputStream is1 = new FileInputStream(file1);
        ImageHolder image1 = new ImageHolder(is1, file1.getName());

        File file2 = new File("E:/cww/Carousel2.jpg");
        InputStream is2 = new FileInputStream(file2);
        ImageHolder image2 = new ImageHolder(is2, file2.getName());

        File file3 = new File("E:/cww/Carousel3.jpg");
        InputStream is3 = new FileInputStream(file3);
        ImageHolder image3 = new ImageHolder(is3, file3.getName());
        List<ImageHolder> imageHolderList = new ArrayList<>();
        imageHolderList.add(image1);
        imageHolderList.add(image2);
        ProductExecution pe = server.addProduct(product, image3, imageHolderList);
        assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
    }

    @Test
    public void TestBupdateProduct() throws FileNotFoundException {
        Product product = new Product();
        product.setProductId(2L);
        Shop shop = new Shop();
        shop.setShopId(7L);
        product.setShop(shop);
        product.setProductName("橙汁汁");

        File file1 = new File("E:/cww/a.jpg");
        InputStream is1 = new FileInputStream(file1);
        ImageHolder image1 = new ImageHolder(is1, file1.getName());

        File file2 = new File("E:/cww/Carousel6.jpg");
        InputStream is2 = new FileInputStream(file2);
        ImageHolder image2 = new ImageHolder(is2, file2.getName());
        List<ImageHolder> imageHolderList = new ArrayList<>();
        imageHolderList.add(image2);
        ProductExecution pe = server.modifyProduct(product, image1, imageHolderList);
        assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());

    }

}
