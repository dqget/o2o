package com.lovesickness.o2o.dao;

import com.lovesickness.o2o.entity.WechatAuth;
import org.springframework.stereotype.Repository;

@Repository
public interface WechatAuthDao {
    /**
     * 通过openid查询本平台的微信账号
     *
     * @param openId
     * @return
     */
    WechatAuth queryWechatInfoByOpenId(String openId);

    /**
     * 添加对应平台的微信账号
     *
     * @param wechatAuth
     * @return
     */
    int insertWechatAuth(WechatAuth wechatAuth);

}
