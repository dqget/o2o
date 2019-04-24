package com.lovesickness.o2o.web.wechat;

import com.lovesickness.o2o.dto.UserAccessToken;
import com.lovesickness.o2o.dto.WechatAuthExecution;
import com.lovesickness.o2o.dto.WechatUser;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.WechatAuth;
import com.lovesickness.o2o.enums.WechatAuthStateEnum;
import com.lovesickness.o2o.service.PersonInfoService;
import com.lovesickness.o2o.service.WechatAuthService;
import com.lovesickness.o2o.util.weixin.WechatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("wechatlogin")
@Api(tags = "WechatLoginController|微信登录控制器")
/**
 * 获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2f51d86feea57077&redirect_uri=http://www.dqzdh.top/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=2#wechat_redirect
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 *
 * @author 懿
 *
 */
public class WechatLoginController {

    public static final String FRONTEND = "1";
    public static final String SHOPEND = "2";
    private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
    @Autowired
    private PersonInfoService personInfoService;
    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping(value = "/logincheck", method = {RequestMethod.GET})
    @ApiOperation(value = "微信登录接口", notes = "在微信里通过访问网址回调到该接口")
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("weChat login get...");
        // 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
        String code = request.getParameter("code");
        // 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
        // 点击前端界面state=1  点击店家管理界面state=2
        String userType = request.getParameter("state");
        log.debug("weChat login code:" + code);
        WechatUser user;
        String openId;
        WechatAuth wechatAuth;
        if (null != code) {
            // 通过code获取access_token
            UserAccessToken token = WechatUtil.getUserAccessToken(code);
            log.debug("通过code从微信获取到的access_token:" + token);
            assert token != null;
            // 通过token获取accessToken
            String accessToken = token.getAccessToken();
            // 获取token中的openId
            openId = token.getOpenId();
            // 通过access_token和openId获取用户昵称等信息
            user = WechatUtil.getUserInfo(accessToken, openId);
            log.debug("从微信获取到的用户信息:" + user);
            assert user != null;
            request.getSession().setAttribute("openId", openId);
            //通过openId查询微信账号信息
            wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);

            //wechatAuth==null表示微信用户是第一次登录
            if (wechatAuth == null) {
                log.info("该用户首次登录");
                PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
                wechatAuth = new WechatAuth();
                wechatAuth.setOpenId(user.getOpenId());
                if (FRONTEND.equals(userType)) {
                    personInfo.setUserType(1);
                } else {
                    personInfo.setUserType(2);
                }
                wechatAuth.setPersonInfo(personInfo);
                WechatAuthExecution we = wechatAuthService.register(wechatAuth);
                if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
                    return null;
                } else {
                    personInfo = personInfoService.getPersonInfoById(wechatAuth.getPersonInfo().getUserId());
                    request.getSession().setAttribute("user", personInfo);
                    log.info("将用户信息放入session " + personInfo);
                }
            } else {
                PersonInfo personInfo = wechatAuth.getPersonInfo();
                log.info("该用户不是首次登录，个人信息:" + personInfo);
                request.getSession().setAttribute("user", personInfo);
                log.info("将用户信息放入session " + personInfo);
            }
        }
        //若用户点击的是前端展示系统按钮则进入前端展示系统
        if (FRONTEND.equals(userType)) {
            return "frontend/index";
        } else if (SHOPEND.equals(userType)) {
            return "shop/shoplist";
        } else {
            return null;
        }
    }
}

