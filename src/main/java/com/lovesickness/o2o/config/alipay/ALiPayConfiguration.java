package com.lovesickness.o2o.config.alipay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author 懿
 */
@Configuration
public class ALiPayConfiguration {
    /**
     * 阿里支付APPID
     */
    public static String appId;
    /**
     * 商户支付密钥
     */
    public static String merchantPrivateKey;
    /**
     * 公钥
     */
    public static String aliPublicKry;
    /**
     * 支付网关
     */
    public static String gateWayUrl;
    /**
     * 签名类型
     */
    public static String signType;

    @Value(value = "ali.app_id")
    public void setAppId(String appId) {
        ALiPayConfiguration.appId = appId;
    }

    @Value(value = "ali.merchant_private_key")

    public void setMerchantPrivateKey(String merchantPrivateKey) {
        ALiPayConfiguration.merchantPrivateKey = merchantPrivateKey;
    }

    @Value(value = "ali.alipay_public_key")
    public void setAliPublicKry(String aliPublicKry) {
        ALiPayConfiguration.aliPublicKry = aliPublicKry;
    }

    @Value(value = "ali.gateyay.url")
    public void setGateWayUrl(String gateWayUrl) {
        ALiPayConfiguration.gateWayUrl = gateWayUrl;
    }

    @Value(value = "ali.sign_type")
    public void setSignType(String signType) {
        ALiPayConfiguration.signType = signType;
    }

}
