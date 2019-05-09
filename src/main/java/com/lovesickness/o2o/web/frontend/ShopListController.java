package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.dto.ShopExecution;
import com.lovesickness.o2o.entity.Area;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.ShopCategory;
import com.lovesickness.o2o.service.AreaService;
import com.lovesickness.o2o.service.ShopCategoryService;
import com.lovesickness.o2o.service.ShopService;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "ShopListController|前端展示系统的店铺信息查询控制器")
public class ShopListController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;

    @GetMapping(value = "/listshoppageinfo")
    @ApiOperation(value = "查询店铺分类列表和区域列表", notes = "查询店铺分类列表和区域列表，如果店铺分类不存在，取出所有一级ShopCategory(用户在首页选择的是全部商品列表，如果是一级ShopCategoryId则取出该一级ShopCategory下的二级ShopCategory列表")
    public Map listShopPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(8);
        List<ShopCategory> shopCategoryList = null;
        Long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        //如果parentId存在，取出该一级ShopCategory下的二级ShopCategory列表
        if (parentId != null) {
            try {
                ShopCategory childShopCategory = new ShopCategory();
                ShopCategory parentShopCategory = new ShopCategory();
                parentShopCategory.setShopCategoryId(parentId);
                childShopCategory.setParent(parentShopCategory);
                shopCategoryList = shopCategoryService.getShopCategoryList(childShopCategory);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            //如果parentId不存在，取出所有一级ShopCategory(用户在首页选择的是全部商品列表)
            try {
                shopCategoryList = shopCategoryService.getShopCategoryList(null);

            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        modelMap.put("shopCategoryList", shopCategoryList);
        List<Area> areaList;
        try {
            areaList = areaService.getAreaList();
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @GetMapping(value = "/listshop")
    @ApiOperation(value = "根据店铺分类查询店铺列表", notes = "查询该店铺分类下的店铺列表")
    public Map listShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Integer pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Integer pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        if ((pageIndex != null) && (pageSize != null)) {
            //一级分类Id
            Long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            //二级分类Id
            Long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            Long areaId = HttpServletRequestUtil.getLong(request, "areaId");
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            Shop shopCondition = compactShopCondition(parentId, shopCategoryId, areaId, shopName);
            ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
            modelMap.put("shopList", se.getShopList());
            modelMap.put("count", se.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex");
        }
        return modelMap;
    }

    private Shop compactShopCondition(Long parentId,
                                      Long shopCategoryId, Long areaId, String shopName) {
        Shop shopCondition = new Shop();
        if (parentId != null) {
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if (shopCategoryId != null) {
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId != null) {
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }

        if (shopName != null) {
            shopCondition.setShopName(shopName);
        }
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
