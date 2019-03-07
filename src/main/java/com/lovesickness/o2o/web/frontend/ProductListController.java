package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.service.ProductService;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/frontend")
public class ProductListController {
    @Autowired
    private ProductService productService;

    @GetMapping(value = "listproductdetailpageinfo")
    public ResultBean<Product> listProductDetailPageInfo(HttpServletRequest request) {
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        return new ResultBean<>(productService.getProductById(productId));
    }
}
