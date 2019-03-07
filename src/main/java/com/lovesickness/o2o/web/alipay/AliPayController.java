package com.lovesickness.o2o.web.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePayRequest;
import com.lovesickness.o2o.config.alipay.ALiPayConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 懿
 */
@Controller
@RequestMapping("/pay")
public class AliPayController {
    @PostMapping("/alipay")
    public void aliPay() {
        AlipayClient alipayClient = new DefaultAlipayClient(ALiPayConfiguration.gateWayUrl, ALiPayConfiguration.appId, ALiPayConfiguration.merchantPrivateKey, "json", "utf-8", ALiPayConfiguration.aliPublicKry, ALiPayConfiguration.signType);
        AlipayTradePayRequest alipqyRequest = new AlipayTradePayRequest();
    }
}
