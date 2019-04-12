package com.lovesickness.o2o.web.alipay;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pay")
@Api(tags = "PayAdminController|支付、订单界面跳转的控制器")
public class PayAdminController {
    @GetMapping(value = "/alipay")
    @ApiOperation(value = "跳转到支付确认界面")
    public String openAlipay() {
        return "pay/alipay";
    }

    @GetMapping(value = "/order")
    @ApiOperation(value = "订单界面")
    public String pay() {
        return "pay/order";
    }
}
