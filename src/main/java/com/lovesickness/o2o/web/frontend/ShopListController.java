package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.dto.ShopExecution;
import com.lovesickness.o2o.entity.Area;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.ShopCategory;
import com.lovesickness.o2o.service.AreaService;
import com.lovesickness.o2o.service.ShopCategoryService;
import com.lovesickness.o2o.service.ShopService;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
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
public class ShopListController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;

    @GetMapping(value = "/listshoppageinfo")
    public Map listShopPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = null;
        Long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        //如果parentId存在，取出该一级ShopCategory下的二级ShopCategory列表
        if (parentId != -1) {
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
        List<Area> areaList = null;
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
    public Map listShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        if ((pageIndex > -1) && (pageIndex > -1)) {
            //一级分类Id
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            //二级分类Id
            long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            int areaId = HttpServletRequestUtil.getInt(request, "areaId");
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

    private Shop compactShopCondition(long parentId,
                                      long shopCategoryId, long areaId, String shopName) {
        Shop shopCondition = new Shop();
        if (parentId != -1L) {
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if (shopCategoryId != -1L) {
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId != -1L) {
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }

        if (shopName != null) {
            shopCondition.setShopName(shopName);
        }
        shopCondition.setEnableStatus(2);
        return shopCondition;
    }
}
