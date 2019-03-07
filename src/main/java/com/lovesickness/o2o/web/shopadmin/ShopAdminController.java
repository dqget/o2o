package com.lovesickness.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 懿
 */
@Controller
@RequestMapping(value = "/shopadmin", method = {RequestMethod.GET})
public class ShopAdminController {
    @RequestMapping(value = "/shopoperation")
    public String shopOperation() {
        return "shop/shopoperation";
    }

    @RequestMapping(value = "/shoplist")
    public String shopList() {
        return "shop/shoplist";
    }

    @RequestMapping(value = "/shopmanage")
    public String shopManage() {
        return "shop/shopmanage";
    }

    @RequestMapping(value = "/productcategorymanagement")
    public String productCategoryList() {
        return "shop/productcategorylist";
    }

    @RequestMapping(value = "/productoperation")
    public String productOperation() {
        return "shop/productoperation";
    }

    @RequestMapping(value = "/productlist")
    public String productList() {
        return "shop/productlist";
    }

    @RequestMapping(value = "/shopauthmanagement")
    public String shopAuthList() {
        return "shop/shopauthmanagement";
    }

    @RequestMapping(value = "/shopauthedit")
    public String shopauthEdit() {
        return "shop/shopauthedit";
    }

    @RequestMapping(value = "/productbuycheck")
    public String productBuyCheck() {
        return "shop/productbuycheck";
    }

    /**
     * 转发到操作成功页面
     *
     * @return
     */
    @RequestMapping(value = "/operationsuccess")
    public String operationSuccess() {
        return "local/operationsuccess";
    }

    /**
     * 转发到操作失败页面
     *
     * @return
     */
    @RequestMapping(value = "/operationfail")
    public String operationFail() {
        return "local/operationfail";
    }
}
