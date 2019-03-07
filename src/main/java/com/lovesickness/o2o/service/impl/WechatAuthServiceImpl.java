package com.lovesickness.o2o.service.impl;

import com.lovesickness.o2o.dao.PersonInfoDao;
import com.lovesickness.o2o.dao.WechatAuthDao;
import com.lovesickness.o2o.dto.WechatAuthExecution;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.WechatAuth;
import com.lovesickness.o2o.enums.WechatAuthStateEnum;
import com.lovesickness.o2o.exception.WechatAuthOperationException;
import com.lovesickness.o2o.service.WechatAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author 懿
 */
@Service
public class WechatAuthServiceImpl implements WechatAuthService {
    private static Logger logger = LoggerFactory.getLogger(WechatAuthServiceImpl.class);
    @Autowired
    private WechatAuthDao wechatAuthDao;
    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public WechatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthDao.queryWechatInfoByOpenId(openId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException {
        if (wechatAuth == null || wechatAuth.getOpenId() == null) {
            return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
        }
        try {
            wechatAuth.setCreateTime(new Date());
            //如果微信账号里夹带的用户信息并且用户Id为空，则认为该用户第一次使用平台（且微信登陆）
            //则自动创建用户信息
            if (wechatAuth.getPersonInfo() != null && wechatAuth.getPersonInfo().getUserId() == null) {
                try {
                    wechatAuth.getPersonInfo().setCreateTime(new Date());
                    wechatAuth.getPersonInfo().setEnableStatus(1);
                    wechatAuth.getPersonInfo().setLastEditTime(new Date());
                    PersonInfo personInfo = wechatAuth.getPersonInfo();
                    int effectedNum = personInfoDao.insertPersonInfo(personInfo);
                    wechatAuth.setPersonInfo(personInfo);
                    if (effectedNum <= 0) {
                        throw new WechatAuthOperationException("添加用户信息失败");
                    }
                } catch (Exception e) {
                    logger.error("insertPersonInfo error : " + e.toString());
                    throw new WechatAuthOperationException("insertPersonInfo error : " + e.getMessage());
                }
            }
            //创建专属于本平台的微信账号
            int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
            if (effectedNum <= 0) {
                throw new WechatAuthOperationException("账号创建失败");
            } else {
                return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS, wechatAuth);
            }
        } catch (Exception e) {
            logger.error("insertWechatAuth error : " + e.toString());
            throw new WechatAuthOperationException("insertWechatAuth error : " + e.getMessage());
        }
    }
}
