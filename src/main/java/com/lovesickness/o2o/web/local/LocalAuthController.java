package com.lovesickness.o2o.web.local;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.lovesickness.o2o.config.wechat.WechatConfiguration;
import com.lovesickness.o2o.dto.ImageHolder;
import com.lovesickness.o2o.dto.LocalAuthExecution;
import com.lovesickness.o2o.entity.LocalAuth;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.enums.LocalAuthStateEnum;
import com.lovesickness.o2o.exception.LocalAuthOperationException;
import com.lovesickness.o2o.service.LocalAuthService;
import com.lovesickness.o2o.util.CodeUtil;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ShopNetAddressUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 懿
 */
@RestController
@RequestMapping(value = "local", method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {
    private static Logger logger = LoggerFactory.getLogger(LocalAuthController.class);
    @Autowired
    private LocalAuthService localAuthService;

    @PostMapping(value = "/logincheck")
    private Map<String, Object> loginCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
        if (needVerify && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");
        if (userName != null && password != null) {
            LocalAuth localAuth = localAuthService.getLocalAuthByUserNameAndPwd(userName, password);
            if (localAuth != null) {
                modelMap.put("success", true);
                modelMap.put("user", localAuth.getPersonInfo());
                request.getSession().setAttribute("user", localAuth.getPersonInfo());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名或密码错误");
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码均不能为空");
        }
        return modelMap;
    }

    @RequestMapping(value = "/ownerregister", method = RequestMethod.POST)
    private Map<String, Object> ownerRegister(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        ObjectMapper mapper = new ObjectMapper();
        LocalAuth localAuth = null;
        String localAuthStr = HttpServletRequestUtil.getString(request,
                "localAuthStr");
        MultipartHttpServletRequest multipartRequest = null;
        CommonsMultipartFile profileImg = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            multipartRequest = (MultipartHttpServletRequest) request;
            profileImg = (CommonsMultipartFile) multipartRequest
                    .getFile("thumbnail");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        try {
            localAuth = mapper.readValue(localAuthStr, LocalAuth.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        if (localAuth != null && localAuth.getPassword() != null
                && localAuth.getUserName() != null) {
            try {
                PersonInfo user = (PersonInfo) request.getSession()
                        .getAttribute("user");
                if (user != null && localAuth.getPersonInfo() != null) {
                    localAuth.getPersonInfo().setUserId(user.getUserId());
                }
                localAuth.getPersonInfo().setUserType(2);

                LocalAuthExecution le = localAuthService.register(localAuth,
                        new ImageHolder(profileImg.getInputStream(), profileImg.getOriginalFilename()));
                if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", le.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入注册信息");
        }
        return modelMap;
    }


    @PostMapping(value = "/bindlocalauth")
    private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>(8);
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");
        //从session中获取用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (userName != null && password != null && user != null && user.getUserId() != null) {
            //创建localauth对象
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUserName(userName);
            localAuth.setPassword(password);
            localAuth.setPersonInfo(user);

            LocalAuthExecution le = localAuthService.bindLocalAuth(localAuth);
            if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", le.getStateInfo());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码均不能为空");
        }
        return modelMap;
    }

    @PostMapping(value = "/changelocalpwd")
    private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (userName != null && password != null && newPassword != null
                && user != null && user.getUserId() != null && !password.equals(newPassword)) {
            try {
                //查看原先账号，看看与输入的账号是否一致，不一致认为是非法操作
                LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
                if (localAuth == null || !localAuth.getUserName().equals(userName)) {
                    //不一致则退出
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "输入账号非本次登录账号");
                    return modelMap;
                }
                //修改平台账号的密码
                LocalAuthExecution le = localAuthService.modifyLocalAuth(user.getUserId(), userName, password,
                        newPassword);
                if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", le.getStateInfo());
                }
            } catch (LocalAuthOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入密码");
        }
        return modelMap;
    }

    @GetMapping("/generateqrcode4wechatlogin")
    public void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response) {
        // 1 表示登录前端系统
        // 2 表示登录店家系统
        String usertype = HttpServletRequestUtil.getString(request, "usertype");
        try {
            String longUrl = WechatConfiguration.urlPrefix
                    + WechatConfiguration.loginUrl
                    + WechatConfiguration.urlMiddle
                    + usertype
                    + WechatConfiguration.urlSuffix;
            logger.debug("二维码生成的长网址为：" + longUrl);
            //将目标URL转换为短的URL
            String shortUrl = ShopNetAddressUtil.getShortURL(longUrl);
            //调用二维码生成工具类的方法，传入短的URL，生成二维码
            BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(shortUrl, response);
            assert qRcodeImg != null;
            //将二维码以流的形式输出到前端
            MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    private Map<String, Object> logoutCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        request.getSession().setAttribute("user", null);
        request.getSession().setAttribute("shopList", null);
        request.getSession().setAttribute("currentShop", null);
        modelMap.put("success", true);
        return modelMap;
    }
}
