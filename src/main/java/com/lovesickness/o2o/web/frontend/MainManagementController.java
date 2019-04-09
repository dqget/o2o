package com.lovesickness.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frontend")
public class MainManagementController {
    @GetMapping(value = "/index")
    private String index() {
        return "frontend/index";
    }

    @GetMapping(value = "/shoplist")
    private String shoplist() {
        return "frontend/shoplist";
    }

    @GetMapping(value = "/shopdetail")
    public String shopDetail() {
        return "frontend/shopdetail";
    }

    @GetMapping(value = "/productdetail")
    public String productDetail() {
        return "frontend/productdetail";
    }

    @GetMapping(value = "/awardlist")
    public String awardList() {
        return "frontend/shopaward";
    }

    @GetMapping(value = "/buyercart")
    public String buyerCart() {
        return "frontend/buyercart";
    }

    @GetMapping(value = "/orderlist")
    public String orderList() {
        return "frontend/orderlist";
    }


}
