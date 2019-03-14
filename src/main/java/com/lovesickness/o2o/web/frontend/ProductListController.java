package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.service.ProductService;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/frontend")
public class ProductListController {
    @Autowired
    private ProductService productService;

    @GetMapping(value = "listproductdetailpageinfo")
    public Map<String, Object> listProductDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(8);
        Long productId = HttpServletRequestUtil.getLong(request, "productId");
        Product product;
        if (productId != null) {
            product = productService.getProductById(productId);
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            if (user == null) {
                modelMap.put("needQRCode", false);
            } else {
                modelMap.put("needQRCode", true);

            }
            modelMap.put("product", product);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty productId");

        }
        return modelMap;
    }
}
