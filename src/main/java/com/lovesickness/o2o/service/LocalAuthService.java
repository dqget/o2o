package com.lovesickness.o2o.service;


import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.dto.LocalAuthExecution;
import com.lovesickness.o2o.entity.LocalAuth;
import com.lovesickness.o2o.exception.LocalAuthOperationException;

public interface LocalAuthService {
    /**
     * 通过账号密码获取平台信息
     *
     * @param userName
     * @return
     */
    LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password);

    /**
     * 通过userId获取用户信息
     *
     * @param userId
     * @return
     */
    LocalAuth getLocalAuthByUserId(long userId);

    /**
     * @param localAuth
     * @param profileImg
     * @return
     * @throws RuntimeException
     */
    LocalAuthExecution register(LocalAuth localAuth,
                                ImageHolder profileImg) throws RuntimeException;

    /**
     * 绑定微信,生产平台专属的账号
     *
     * @param localAuth
     * @return
     * @throws LocalAuthOperationException
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth)
            throws LocalAuthOperationException;

    /**
     * 修改平台登录密码
     *
     * @param userId
     * @param userName
     * @param password
     * @param newPassword
     * @return
     */
    LocalAuthExecution modifyLocalAuth(Long userId, String userName,
                                       String password, String newPassword) throws LocalAuthOperationException;
}
