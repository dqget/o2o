package com.lovesickness.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.dto.ProductExecution;
import com.lovesickness.o2o.entity.Product;
import com.lovesickness.o2o.entity.ProductCategory;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.service.ProductCategoryService;
import com.lovesickness.o2o.service.ProductService;
import com.lovesickness.o2o.util.CodeUtil;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 懿
 */
@RestController
@RequestMapping("/shopadmin")
@Api(tags = "ProductManagementController|商品管理控制器")
public class ProductManagementController {
    /**
     * 商品详情图允许最大上传数量
     */
    private static final int IMAGEMAXCOUNT = 6;
    private static Logger LOGGER = LoggerFactory.getLogger(ProductManagementController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    @PostMapping(value = "/addproduct")
    @ApiOperation(value = "添加商品", notes = "向店铺添加一个商品信息，需要输入验证码")
    public ResultBean<ProductExecution> addProduct(HttpServletRequest request) {
        //校验验证码
        if (!CodeUtil.checkVerifyCode(request)) {
            return new ResultBean<>(false, 0, "验证码输入错误");
        }
        ObjectMapper mapper = new ObjectMapper();
        Product product;
        //获取到前端传过来的productJson
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        //用来处理文件流
        MultipartHttpServletRequest multipartRequest;
        ImageHolder thumbnail;
        List<ImageHolder> productImgList = new ArrayList<>();
        //从request session获取到文件流
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            if (multipartResolver.isMultipart(request)) {
                multipartRequest = (MultipartHttpServletRequest) request;
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
                assert thumbnailFile != null;
                thumbnail = new ImageHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());
                //取出商品详情图构建List<ImageHolder>对象
                handleProductImgList(multipartRequest, productImgList);
            } else {
                return new ResultBean<>(false, 0, "上传图片不能为空");
            }
        } catch (Exception e) {
            LOGGER.error("addProduct error : " + e.toString());
            e.printStackTrace();
            return new ResultBean<>(e);
        }
        try {
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            LOGGER.error("addProduct error : " + e.toString());
            e.printStackTrace();
            return new ResultBean<>(e);
        }
        if (product != null && productImgList.size() > 0) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            Shop shop = new Shop();
            shop.setShopId(currentShop.getShopId());
            product.setShop(shop);
            return new ResultBean<>(productService.addProduct(product, thumbnail, productImgList));
        }
        return new ResultBean<>(false, 0, "请输入商品信息");
    }


    @PostMapping(value = "/modifyproduct")
    @ApiOperation(value = "修改商品", notes = "修改商品信息，需要输入验证码")
    public ResultBean<ProductExecution> modifyProduct(HttpServletRequest request) {
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");

        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            return new ResultBean<>(false, 0, "验证码输入错误");
        }
        ObjectMapper mapper = new ObjectMapper();
        Product product;
        //获取到前端传过来的productJson
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        //用来处理文件流
        MultipartHttpServletRequest multipartRequest;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new ArrayList<>();
        //从request session获取到文件流
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            if (commonsMultipartResolver.isMultipart(request)) {
                multipartRequest = (MultipartHttpServletRequest) request;
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
                if (thumbnailFile != null) {
                    thumbnail = new ImageHolder(thumbnailFile.getInputStream(), thumbnailFile.getOriginalFilename());
                }
                //取出商品详情图构建List<ImageHolder>对象
                handleProductImgList(multipartRequest, productImgList);
            }
        } catch (Exception e) {
            LOGGER.error("modifyProduct error : " + e.toString());
            return new ResultBean<>(e);
        }
        try {
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            LOGGER.error("modifyProduct error : " + e.toString());
            return new ResultBean<>(e);
        }
        if (product != null) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            Shop shop = new Shop();
            shop.setShopId(currentShop.getShopId());
            product.setShop(shop);
            return new ResultBean<>(productService.modifyProduct(product, thumbnail, productImgList));
        }
        return new ResultBean<>(false, 0, "请输入商品信息");
    }

    private void handleProductImgList(MultipartHttpServletRequest multipartRequest, List<ImageHolder> productImgList) throws IOException {
        for (int i = 0; i < IMAGEMAXCOUNT; i++) {
            CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
            if (productImgFile != null) {
                ImageHolder productImg = new ImageHolder(productImgFile.getInputStream(), productImgFile.getOriginalFilename());
                productImgList.add(productImg);
            } else {
                break;
            }
        }
    }

    @GetMapping(value = "/getproductbyid")
    @ApiOperation(value = "根据商品Id查询商品信息", notes = "根据商品Id查询商品信息")
    public Map<String, Object> getProductById(@RequestParam Long productId) {
        Map<String, Object> modelMap = new HashMap<>(8);
        if (productId > -1) {
            Product product = productService.getProductById(productId);
            List<ProductCategory> productCategoryList = productCategoryService
                    .getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product", product);
            modelMap.put("productCategoryList", productCategoryList);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    @GetMapping(value = "/getproductlistbyshop")
    @ApiOperation(value = "查询店铺下的商品列表", notes = "查询店铺下的商品列表")
    public ResultBean<ProductExecution> getProductListByShop(HttpServletRequest request) {
        Integer pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        Integer pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if ((pageIndex != null) && (pageSize != null) && (currentShop != null) && (currentShop.getShopId() != null)) {
            Long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product productCondition = compactProductCondition(currentShop.getShopId(), productCategoryId, productName);
            return new ResultBean<>(productService.getProductList(productCondition, pageIndex, pageSize));
        } else {
            return new ResultBean<>(false, 0, "empty pageSize or pageIndex or shopId");
        }

    }

    @GetMapping(value = "/getproductavestar")
    @ApiOperation(value = "查询店铺下的商品列表", notes = "查询店铺下的商品列表")
    public ResultBean<List<Product>> getProductAveStar(HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if ((currentShop != null) && (currentShop.getShopId() != null)) {
            return new ResultBean<>(productService.getProductAveStar(currentShop.getShopId()));
        } else {
            return new ResultBean<>(false, 0, "请重新登录");
        }

    }

    private Product compactProductCondition(Long shopId, Long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if (productCategoryId != null) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName != null) {
            productCondition.setProductName(productName);
        }
        return productCondition;
    }
}
