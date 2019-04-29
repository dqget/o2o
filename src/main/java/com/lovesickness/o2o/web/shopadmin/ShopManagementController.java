package com.lovesickness.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.dto.ShopExecution;
import com.lovesickness.o2o.entity.Area;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.ShopCategory;
import com.lovesickness.o2o.enums.ShopStateEnum;
import com.lovesickness.o2o.exception.ShopOperationException;
import com.lovesickness.o2o.service.AreaService;
import com.lovesickness.o2o.service.ShopCategoryService;
import com.lovesickness.o2o.service.ShopService;
import com.lovesickness.o2o.util.CodeUtil;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
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
@Controller
@RequestMapping("/shopadmin")
@Api(tags = "ShopManagementController|商品信息管理控制器")
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    @GetMapping(value = "/getshopbyid")
    @ResponseBody
    @ApiOperation(value = "根据店铺Id查询店铺信息", notes = "根据店铺Id查询店铺信息")
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId != null) {
            try {
                Shop shop = shopService.queryShopById(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @PostMapping(value = "/modifyshop")
    @ResponseBody
    @ApiOperation(value = "修改店铺信息", notes = "修改店铺信息需要输入验证码")
    private Map<String, Object> modifyShop(@Param("shopImg") MultipartFile shopImg, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(8);
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("succese", false);
            modelMap.put("errMsg", "验证码输入错误");
            return modelMap;
        }
        // 1.接受并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("succese", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        // 2.修改店铺
        if (shop != null && shop.getShopId() != null) {
            ShopExecution se;
            try {
                if (shopImg != null) {
                    ImageHolder image = new ImageHolder(shopImg.getInputStream(), shopImg.getOriginalFilename());
                    se = shopService.modifyShop(shop, image);
                } else {
                    se = shopService.modifyShop(shop, null);
                }
                if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (ShopOperationException | IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());

            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺Id");
            return modelMap;
        }
    }

    @GetMapping(value = "/getshopinitinfo")
    @ResponseBody
    @ApiOperation(value = "获取店铺分类列表和区域列表", notes = "添加店铺时使用，获取店铺分类列表和区域列表")
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<>(8);
        List<ShopCategory> shopCategoryList;
        List<Area> areaList;
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @PostMapping(value = "/registershop")
    @ResponseBody
    @ApiOperation(value = "注册店铺", notes = "添加店铺需要输入验证码")
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(8);
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("succese", false);
            modelMap.put("errMsg", "验证码输入错误");
            return modelMap;
        }
        // 1.接受并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("succese", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg;
        // 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        // 判断是否是多数据段提交格式
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        // 2.注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);

            ShopExecution se;
            try {
                se = shopService.addShop(shop, new ImageHolder(shopImg.getInputStream(), shopImg.getOriginalFilename()));
                if (se.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    @SuppressWarnings("unchecked")
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList", shopList);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (ShopOperationException | IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());

            }

            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }

    @GetMapping(value = "/getshoplist")
    @ResponseBody
    @ApiOperation(value = "获取店铺列表", notes = "获取该用户下的店铺列表")
    public Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(8);
        PersonInfo user;
        user = (PersonInfo) request.getSession().getAttribute("user");

        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution shopExecution = shopService.getShopList(shopCondition, 0, 100);

            request.getSession().setAttribute("shopList", shopExecution.getShopList());
            modelMap.put("shopList", shopExecution.getShopList());
            modelMap.put("user", user);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }

        return modelMap;
    }

    @GetMapping(value = "/getshopmanagementinfo")
    @ResponseBody
    @ApiOperation(value = "将点击的店铺信息添加到session", notes = "将点击的店铺信息添加到session")
    private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(8);
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId == null) {
            Object currentShopObj = request.getSession().getAttribute("currentShop");
            if (currentShopObj == null) {
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopadmin/shoplist");
            } else {
                Shop shop = (Shop) currentShopObj;
                modelMap.put("redirect", false);
                modelMap.put("shopId", shop.getShopId());
            }
        } else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirect", false);
        }
        return modelMap;
    }

}
