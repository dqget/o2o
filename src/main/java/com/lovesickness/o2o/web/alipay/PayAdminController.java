package com.lovesickness.o2o.web.alipay;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pay")
public class PayAdminController {
    @RequestMapping(value = "/alipay")
    public String openAlipay() {
        return "pay/alipay";
    }

    @GetMapping(value = "/order")
    public String pay() {
        return "pay/order";
    }
}
