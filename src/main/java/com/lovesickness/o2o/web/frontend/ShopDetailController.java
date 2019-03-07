package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.dto.ProductExecution;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.entity.ProductCategory;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.service.ProductCategoryService;
import com.lovesickness.o2o.service.ProductService;
import com.lovesickness.o2o.service.ShopService;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frontend")
public class ShopDetailController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 根据shopid查询店铺信息及店铺下的productCategory列表信息
     */
    @GetMapping(value = "/listshopdetailpageinfo")
    public Map<String, Object> shopDetail(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Shop shop;
        List<ProductCategory> productCategoryList;
        try {
            if (shopId <= -1L) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "empty shopId or pageIndex or pageSize");
                return modelMap;
            }
            shop = shopService.queryShopById(shopId);
            productCategoryList = productCategoryService.getProductCategoryList(shopId);
            modelMap.put("shop", shop);
            modelMap.put("productCategoryList", productCategoryList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @GetMapping(value = "/listproductbyshop")
    public ResultBean<ProductExecution> listProductByShop(HttpServletRequest request) {
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if ((shopId > -1) && (pageIndex > -1) && (pageSize > -1)) {
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product product = compactProductCondition(shopId, productCategoryId, productName);
            return new ResultBean<>(productService.getProductList(product, pageIndex, pageSize));
        } else {
            return new ResultBean<>(false, 0, "empty shopId or pageIndex  or pageSize");
        }
    }

    private Product compactProductCondition(long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();

        if (shopId != -1L) {
            Shop shop = new Shop();
            shop.setShopId(shopId);
            productCondition.setShop(shop);
            if (productCategoryId != -1L) {
                ProductCategory productCategory = new ProductCategory();
                productCategory.setProductCategoryId(productCategoryId);
                productCondition.setProductCategory(productCategory);
            }
            if (productName != null) {
                productCondition.setProductName(productName);
            }
        }
        productCondition.setEnableStatus(1);
        return productCondition;
    }
}
