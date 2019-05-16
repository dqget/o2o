package com.lovesickness.o2o.web.frontend;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frontend")
@Api(tags = "MainManagementController|前端展示系统界面跳转控制器")
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

    @GetMapping(value = "/orderdetail")
    public String orderDetail() {
        return "frontend/orderdetail";
    }

    @GetMapping(value = "/commentproduct")
    public String commentProduct() {
        return "frontend/commentproduct";
    }

    @GetMapping(value = "/pointrecord")
    public String pointRecord() {
        return "frontend/pointrecord";
    }


    @GetMapping(value = "/addschedule")
    public String addSchedule() {
        return "frontend/addschedule";
    }

    @GetMapping(value = "/schedulelist")
    public String scheduleList() {
        return "frontend/schedulelist";
    }

    @GetMapping(value = "/scheduledistributionlist")
    public String scheduleDistributionList() {
        return "frontend/scheduledistributionlist";
    }

    @GetMapping(value = "/distributionoperation")
    public String distributionOperation() {
        return "frontend/distributionoperation";
    }
}
