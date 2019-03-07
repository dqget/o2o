package com.lovesickness.o2o.service;

import com.lovesickness.o2o.dto.WechatAuthExecution;
import com.lovesickness.o2o.entity.WechatAuth;
import com.lovesickness.o2o.exception.WechatAuthOperationException;

public interface WechatAuthService {
    /**
     * 通过openId查询平台对应的微信账号
     *
     * @param openId
     * @return
     */
    WechatAuth getWechatAuthByOpenId(String openId);

    /**
     * 注册本平台的微信账号
     *
     * @param wechatAuth
     * @return
     * @throws RuntimeException
     */
    WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;
}
