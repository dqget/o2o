package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.dao.HeadLineDao;
import com.lovesickness.o2o.dao.ShopCategoryDao;
import com.lovesickness.o2o.entity.HeadLine;
import com.lovesickness.o2o.entity.ShopCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 懿
 */
@RestController
@RequestMapping("/frontend")
@Api(tags = "MainPageController|前端展示系统控制器")
public class MainPageController {
    @Autowired
    private HeadLineDao headLineDao;
    @Autowired
    private ShopCategoryDao shopCategoryDao;

    /**
     * 初始化前端展示系统的主页信息，包括一级店铺分类列表和头条信息
     */
    @GetMapping(value = "/listmainpageinfo")
    @ApiOperation(value = "获取主页信息",notes = "包括一级店铺分类列表和头条信息")
    public Map<String, Object> listMainPageInfo() {
        Map<String, Object> modelMap = new HashMap<>(8);
        List<ShopCategory> shopCategoryList;
        try {
            //获取一级店铺分类列表
            shopCategoryList = shopCategoryDao.queryShopCategory(null);
            modelMap.put("shopCategoryList", shopCategoryList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        List<HeadLine> headLineList;
        try {
            //获取enableStatus等于1的头条信息
            HeadLine headLineCondition = new HeadLine();
            headLineCondition.setEnableStatus(1);
            headLineList = headLineDao.queryHeadLine(headLineCondition);
            modelMap.put("headLineList", headLineList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        modelMap.put("success", true);
        return modelMap;
    }


}

