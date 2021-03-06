package com.lovesickness.o2o.web.shopadmin;

import com.lovesickness.o2o.dto.ProductCategoryExecution;
import com.lovesickness.o2o.entity.ProductCategory;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.exception.ProductCategoryOperationException;
import com.lovesickness.o2o.service.ProductCategoryService;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "ProductCategoryManagementController|商品分类管理控制器")
public class ProductCategoryManagementController {
    private static Logger logger = LoggerFactory.getLogger(ProductCategoryManagementController.class);
    @Autowired
    private ProductCategoryService service;

    @GetMapping(value = "/getproductcategorylist")
    @ApiOperation(value = "获取店铺下的商品分类列表", notes = "获取session中店铺下的商品分类列表")
    public ResultBean<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        return new ResultBean<>(service.getProductCategoryList(currentShop.getShopId()));
    }

    @PostMapping(value = "/addproductcategory")
    @ApiOperation(value = "添加商品分类", notes = "向该店铺下批量添加商品分类")
    public ResultBean<ProductCategoryExecution> addProductCategory(@RequestBody List<ProductCategory> productCategoryList, HttpServletRequest request) {
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
    @ApiOperation(value = "删除商品分类", notes = "向该店铺下删除商品分类")
    public ResultBean<ProductCategoryExecution> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
//        request.getSession().invalidate();
        if (productCategoryId == null || productCategoryId < 0) {
            return new ResultBean<>(false, 0, "商品类别错误");
        }
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        return new ResultBean<>(service.deleteProductCategory(productCategoryId, shop.getShopId()));
    }
}
