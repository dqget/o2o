package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.dto.EvaluationExecution;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.service.ProductService;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/frontend")
@Api(tags = "ProductListController|前端商品展示的控制器")
public class ProductListController {
    @Autowired
    private ProductService productService;

    @GetMapping(value = "listproductdetailpageinfo")
    @ApiOperation(value = "根据商品Id查询商品信息", notes = "包括商品的基础信息，不包括评论信息")
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

    @GetMapping("/productevaluationlist")
    @ApiOperation(value = "根据商品Id查询商品评论列表", notes = "查询商品评论信息列表")
    public ResultBean<EvaluationExecution> productEvaluationList(
            @RequestParam(value = "productId") Long productId,
            @RequestParam(value = "pageIndex") Integer pageIndex,
            @RequestParam(value = "pageSize") Integer pageSize) {
        Product product;
        if (productId != null) {
            product = productService.getProductById(productId);
            return new ResultBean<>(productService.getProductEvaluation(product, pageIndex, pageSize));
        } else {
            return new ResultBean<>(false, 0, "商品编号不能为空");
        }

    }
}
