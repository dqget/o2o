package com.lovesickness.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.lovesickness.o2o.config.wechat.WechatConfiguration;
import com.lovesickness.o2o.dto.ShopAuthMapExecution;
import com.lovesickness.o2o.dto.UserAccessToken;
import com.lovesickness.o2o.dto.WechatInfo;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.entity.ShopAuthMap;
import com.lovesickness.o2o.entity.WechatAuth;
import com.lovesickness.o2o.enums.ShopAuthStateEnum;
import com.lovesickness.o2o.exception.ShopAuthMapOperationException;
import com.lovesickness.o2o.service.PersonInfoService;
import com.lovesickness.o2o.service.ShopAuthMapService;
import com.lovesickness.o2o.service.WechatAuthService;
import com.lovesickness.o2o.util.CodeUtil;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import com.lovesickness.o2o.util.ShopNetAddressUtil;
import com.lovesickness.o2o.util.weixin.WechatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;


/**
 * @author 懿
 */
@Controller
@RequestMapping("/shopadmin")
@Api(tags = "ShopAuthManagementController|店铺店员管理控制器")
public class ShopAuthManagementController {
    private static Logger logger = LoggerFactory.getLogger(ShopAuthManagementController.class);


    @Autowired
    private ShopAuthMapService shopAuthMapService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private PersonInfoService personInfoService;

    @GetMapping(value = "/getshopauthmaplistbyshop")
    @ResponseBody
    @ApiOperation(value = "查询店铺下的店员列表", notes = "查询该店铺下的店员列表")
    public ResultBean<ShopAuthMapExecution> getShopAuthMapListByShop(HttpServletRequest request) {
        ResultBean<ShopAuthMapExecution> result;
        Integer pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        Integer pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (pageIndex != null && pageSize != null
                && currentShop != null && currentShop.getShopId() != null) {
            result = new ResultBean<>(shopAuthMapService.getShopAuthMapListByShopId(currentShop.getShopId(), pageIndex, pageSize));
        } else {
            result = new ResultBean<>(new ShopAuthMapOperationException("empty pageSize or pageIndex or shopId"));
        }
        return result;
    }

    @GetMapping(value = "/getshopauthmapbyid")
    @ResponseBody
    @ApiOperation(value = "根据店员Id查询店员信息", notes = "根据店员Id查询店员信息")
    public ResultBean<ShopAuthMap> getShopAuthMapById(@RequestParam Long shopAuthId) {
        ResultBean<ShopAuthMap> result;
        if (shopAuthId != null && shopAuthId > -1L) {
            result = new ResultBean<>(shopAuthMapService.getShopAuthMapById(shopAuthId));
        } else {
            result = new ResultBean<>(new ShopAuthMapOperationException("empty shopAuthId"));
        }
        return result;
    }

    @PostMapping(value = "modifyshopauthmap")
    @ResponseBody
    @ApiOperation(value = "修改店员信息", notes = "根据店员Id查询店员信息")
    public ResultBean<?> modifyShopAuthMap(String shopAuthMapStr, HttpServletRequest request) {
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            return new ResultBean<>(false, 0, "验证码输入错误");
        }
        ObjectMapper mapper = new ObjectMapper();
        ShopAuthMap shopAuthMap;
        try {
            shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
        } catch (IOException e) {
            return new ResultBean<>(e);
        }
        if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
            try {
                if (!checkPermission(shopAuthMap.getShopAuthId())) {
                    return new ResultBean<>(false, 0, "无法对店家本身权限做修改(已是店铺最高权限)");
                }
                ShopAuthMapExecution se = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthStateEnum.SUCCESS.getState()) {
                    return new ResultBean<>(true, 1, "");
                } else {
                    return new ResultBean<>(false, 0, se.getStateInfo());
                }
            } catch (Exception e) {
                return new ResultBean<>(e);
            }
        } else {
            return new ResultBean<>(false, 0, "请输入要修改的授权信息");

        }
    }

    /**
     * 检查配操作的对象是否可以修改
     *
     * @param shopAuthMapId
     * @return
     */
    private boolean checkPermission(Long shopAuthMapId) {
        ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthMapId);
        //店家本身 不能修改
        return shopAuthMap.getTitleFlag() != 0;
    }

    /**
     * 生成带有URL的二维码，微信扫一扫就能链接到对应的URL里
     *
     * @param request
     * @param response
     */
    @GetMapping("/generateqrcode4shopauth")
    @ApiOperation(value = "生成添加店员的二维码", notes = "扫面后添加到店店铺的店员")
    public void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response) {
        //从session获取当前shop信息
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        if (shop != null && shop.getShopId() != null) {
            //获取当前时间戳，以保证二维码时间的有效性，精确到毫秒
            long timeStamp = System.currentTimeMillis();
            String content = "{aaashopIdaaa:" + shop.getShopId() + ",aaacreateTimeaaa:" + timeStamp + "}";
            try {
                String longUrl = WechatConfiguration.urlPrefix
                        + WechatConfiguration.authUrl
                        + WechatConfiguration.urlMiddle
                        + URLEncoder.encode(content, "UTF-8")
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
    }

    /**
     * 根据微信回传回来的参数添加店铺的授权信息
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/addshopauthmap")
    @ApiOperation(value = "根据微信回传回来的参数添加店铺的授权信息", notes = "根据微信回传回来的参数添加店铺的授权信息")
    public String addShopAuthMap(HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户通过扫描二维码欲成为该店的员工");
        //从request里面获取微信用户的信息
        WechatAuth auth = getEmplyeeInfo(request);
        if (auth != null) {
            //根据用户userId获取用户信息
            PersonInfo user = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
            //将用户信息添加到session里
            request.getSession().setAttribute("user", user);
            WechatInfo wechatInfo = null;
            try {
                //解析微信回传过来的自定义参数state ，由于之前进行了编码，这里需要解码一下
                String qrCodeInfo = URLDecoder.decode(Objects.requireNonNull(HttpServletRequestUtil.getString(request, "state")), "UTF-8");
                ObjectMapper mapper = new ObjectMapper();
                //将解码后的内容用\去掉替换掉之前生成二维码的时候加入的aaa前缀,转换为WechatInfo实体类
                wechatInfo = mapper.readValue(qrCodeInfo.replace("aaa", "\""), WechatInfo.class);
            } catch (IOException e) {
                logger.error("addShopAuthMap ：将微信回传回来的信息转为WechatInfo实体类失败");
                e.printStackTrace();
                return "local/operationfail";

            }
            //检查二维码中的信息  和 超时
            if (!checkQRCodeInfo(wechatInfo)) {
                logger.error("addShopAuthMap ：二维码中的信息错误或 超时");
                return "local/operationfail";
            }
            //去重校验
            ShopAuthMapExecution allMapList = shopAuthMapService.getShopAuthMapListByShopId(wechatInfo.getShopId(), 0, 999);
            for (ShopAuthMap sm : allMapList.getShopAuthMapList()) {
                if (sm.getEmployee().getUserId().equals(user.getUserId())) {
                    logger.error("addShopAuthMap ：重复授权同一个微信号");
                    return "local/operationfail";
                }
            }
            try {
                //添加授权信息
                ShopAuthMap shopAuthMap = new ShopAuthMap();
                Shop shop = new Shop();
                shop.setShopId(wechatInfo.getShopId());
                shopAuthMap.setShop(shop);
                shopAuthMap.setEmployee(user);
                shopAuthMap.setTitle("员工");
                ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthStateEnum.SUCCESS.getState()) {
                    return "local/operationsuccess";
                } else {
                    logger.error("addShopAuthMap ：" + se.getStateInfo());
                    return "local/operationfail";
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                return "local/operationfail";
            }
        }
        logger.debug("该微信号未在该平台注册");
        return "local/operationfail";
    }

    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        if (wechatInfo != null && wechatInfo.getShopId() != null && wechatInfo.getCreateTime() != null) {
            long nowTime = System.currentTimeMillis();
            //10分钟超时
            return (nowTime - wechatInfo.getCreateTime()) <= 10 * 60 * 1000;
        } else {
            return false;
        }
    }


    /**
     * 根据微信回传的code获取用户信息
     *
     * @param request
     * @return
     */
    private WechatAuth getEmplyeeInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        WechatAuth auth = null;
        if (code != null) {
            UserAccessToken token;
            token = WechatUtil.getUserAccessToken(code);
            assert token != null;
            String openId = token.getOpenId();
            request.getSession().setAttribute("openId", token.getOpenId());
            auth = wechatAuthService.getWechatAuthByOpenId(openId);
        }
        return auth;

    }
}
