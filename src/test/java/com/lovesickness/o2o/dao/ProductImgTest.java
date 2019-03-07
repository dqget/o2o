package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.ProductImg;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductImgTest {
    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testAbatchInsertProductImg() {
        List<ProductImg> list = new ArrayList<>();
        ProductImg img1 = new ProductImg();
        img1.setImgAddr("imgTestAddr1");
        img1.setCreateTime(new Date());
        img1.setImgDesc("图片描述1");
        img1.setPriority(1);
        img1.setProductId(1L);
        ProductImg img2 = new ProductImg();
        img2.setImgAddr("imgTestAddr2");
        img2.setCreateTime(new Date());
        img2.setImgDesc("图片描述2");
        img2.setPriority(2);
        img2.setProductId(1L);
        ProductImg img3 = new ProductImg();
        img3.setImgAddr("imgTestAddr3");
        img3.setCreateTime(new Date());
        img3.setImgDesc("图片描述3");
        img3.setPriority(3);
        img3.setProductId(1L);
        list.add(img1);
        list.add(img2);
        list.add(img3);
        int effectNum = productImgDao.batchInsertProductImg(list);
        assertEquals(3, effectNum);
    }

    @Test
    public void testBdeleteProductImgByProductId() {
        int effectedNum = productImgDao.deleteProductImgByProductId(1L);
        assertEquals(3, effectedNum);
    }
}
