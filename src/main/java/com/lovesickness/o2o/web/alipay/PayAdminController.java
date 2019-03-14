package com.lovesickness.o2o.web.alipay;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alipay")
public class PayAdminController {
    @RequestMapping(value = "/alipay11")
    public String openAlipay() {
        return "pay/alipay";
    }
}
