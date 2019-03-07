package com.lovesickness.o2o.web.shopadmin;

import com.lovesickness.o2o.dto.ProductCategoryExecution;
import com.lovesickness.o2o.entity.ProductCategory;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.exception.ProductCategoryOperationException;
import com.lovesickness.o2o.service.ProductCategoryService;
import com.lovesickness.o2o.util.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 懿
 */
@RestController
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
    private static Logger logger = LoggerFactory.getLogger(ProductCategoryManagementController.class);
    @Autowired
    private ProductCategoryService service;

    @GetMapping(value = "/getproductcategorylist")
    @ResponseBody
    public ResultBean<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        return new ResultBean<>(service.getProductCategoryList(currentShop.getShopId()));
    }

    @PostMapping(value = "/addproductcategory")
    @ResponseBody
    private ResultBean<ProductCategoryExecution> addProductCategory(@RequestBody List<ProductCategory> productCategoryList, HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (productCategoryList == null || productCategoryList.size() == 0) {
            return new ResultBean<>(new ProductCategoryOperationException("商品类别为空"));
        }
        for (ProductCategory productCategory : productCategoryList) {
            productCategory.setShopId(currentShop.getShopId());
        }
        return new ResultBean<>(service.batchAddProductCategory(productCategoryList));
    }

    @PostMapping(value = "/removeproductcategory")
    @ResponseBody
    private ResultBean<com.lovesickness.o2o.dto.ProductCategoryExecution> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
        request.getSession().invalidate();
        if (productCategoryId == null || productCategoryId < 0) {
            return new ResultBean<>(false, 0, "商品类别错误");
        }
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        return new ResultBean<>(service.deleteProductCategory(productCategoryId, shop.getShopId()));
    }
}
