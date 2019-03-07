package com.lovesickness.o2o.config.wechat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author 懿
 */
@Configuration
public class WechatConfiguration {
    /**
     * 微信获取用户信息的api前缀
     */
    public static String urlPrefix;
    /**
     * 微信获取用户信息的api中间部分
     */
    public static String urlMiddle;
    /**
     * 微信获取用户信息的api后缀
     */
    public static String urlSuffix;
    /**
     * 微信回传给的响应添加授权信息的URL
     */
    public static String authUrl;

    /**
     * 微信登录的URL
     */
    public static String loginUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        WechatConfiguration.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        WechatConfiguration.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        WechatConfiguration.urlSuffix = urlSuffix;
    }

    @Value("${wechat.auth.url}")
    public void setAuthUrl(String authUrl) {
        WechatConfiguration.authUrl = authUrl;
    }

    @Value("${wechat.login.url}")
    public void setLoginUrl(String loginUrl) {
        WechatConfiguration.loginUrl = loginUrl;
    }
}
