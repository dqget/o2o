package com.lovesickness.o2o.web.shopadmin;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 懿
 */
@Controller
@RequestMapping(value = "/shopadmin", method = {RequestMethod.GET})
@Api(tags = "ShopAdminController|后台管理系统界面跳转控制器")
public class ShopAdminController {
    @GetMapping(value = "/shopoperation")
    public String shopOperation() {
        return "shop/shopoperation";
    }

    @GetMapping(value = "/shoplist")
    public String shopList() {
        return "shop/shoplist";
    }

    @GetMapping(value = "/shopmanage")
    public String shopManage() {
        return "shop/shopmanage";
    }

    @GetMapping(value = "/productcategorymanagement")
    public String productCategoryList() {
        return "shop/productcategorylist";
    }

    @GetMapping(value = "/productoperation")
    public String productOperation() {
        return "shop/productoperation";
    }

    @GetMapping(value = "/productlist")
    public String productList() {
        return "shop/productlist";
    }

    @GetMapping(value = "/shopauthmanagement")
    public String shopAuthList() {
        return "shop/shopauthmanagement";
    }

    @GetMapping(value = "/shopauthedit")
    public String shopauthEdit() {
        return "shop/shopauthedit";
    }

    @GetMapping(value = "/productbuycheck")
    public String productBuyCheck() {
        return "shop/productbuycheck";
    }

    @GetMapping(value = "/shoporderlist")
    public String shopOrderList() {
        return "shop/shoporderlist";
    }

    @GetMapping(value = "/shoporderoperation")
    public String shopOrderOperation() {
        return "shop/shoporderoperation";
    }

    @GetMapping(value = "/shopuserpointlist")
    public String shopUserPointList() {
        return "shop/shopuserpointlist";
    }

    @GetMapping(value = "/shopuserawardlist")
    public String shopUserAwardList() {
        return "shop/shopuserawardlist";
    }

    @GetMapping(value = "/awardmanage")
    public String awardManage() {
        return "shop/awardmanage";
    }

    @GetMapping(value = "/awardoperation")
    public String awardOperation() {
        return "shop/awardoperation";
    }

    /**
     * 转发到操作成功页面
     *
     * @return
     */
    @GetMapping(value = "/operationsuccess")
    public String operationSuccess() {
        return "local/operationsuccess";
    }

    /**
     * 转发到操作失败页面
     *
     * @return
     */
    @GetMapping(value = "/operationfail")
    public String operationFail() {
        return "local/operationfail";
    }
}
