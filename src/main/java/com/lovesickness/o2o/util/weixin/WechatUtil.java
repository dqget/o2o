package com.lovesickness.o2o.util.weixin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovesickness.o2o.config.wechat.WechatConfiguration;
import com.lovesickness.o2o.dto.UserAccessToken;
import com.lovesickness.o2o.dto.WechatUser;
import com.lovesickness.o2o.entity.PersonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;

/**
 * 微信工具类
 *
 * @author xiangze
 */
public class WechatUtil {

    private static Logger log = LoggerFactory.getLogger(WechatUtil.class);

    /**
     * 获取UserAccessToken实体类
     *
     * @param code
     * @return
     */
    public static UserAccessToken getUserAccessToken(String code) {
        // 测试号信息里的appId
        String appId = WechatConfiguration.appId;
        log.debug("appId:" + appId);
        // 测试号信息里的appsecret
        String appsecret = WechatConfiguration.appsecret;
        log.debug("appSecret:" + appsecret);
        // 根据传入的code,拼接出访问微信定义好的接口的URL
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appsecret
                + "&code=" + code + "&grant_type=authorization_code";
        log.debug("通过code、appid、secret向微信发起请求获取access_token和openId的URL：" + url);
        // 向相应URL发送请求获取token json字符串
        String tokenStr = httpsRequest(url, "GET", null);
        log.debug("获取到的userAccessToken:" + tokenStr);
        UserAccessToken token = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将json字符串转换成相应对象
            token = objectMapper.readValue(tokenStr, UserAccessToken.class);
        } catch (IOException e) {
            log.error("获取用户accessToken失败: " + e.getMessage());
            e.printStackTrace();
        }
        if (token == null) {
            log.error("获取用户accessToken失败。");
            return null;
        }
        return token;
    }

    /**
     * 获取WechatUser实体类
     *
     * @param accessToken
     * @param openId
     * @return
     */
    public static WechatUser getUserInfo(String accessToken, String openId) {
        // 根据传入的accessToken以及openId拼接出访问微信定义的端口并获取用户信息的URL
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId
                + "&lang=zh_CN";
        log.debug("通过access_token、openid向微信发起请求获取用户信息的URL：" + url);
        // 访问该URL获取用户信息json 字符串
        String userStr = httpsRequest(url, "GET", null);
        log.debug("获取到的用户信息字符串" + userStr);
        WechatUser user = new WechatUser();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将json字符串转换成相应对象
            user = objectMapper.readValue(userStr, WechatUser.class);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取用户信息失败: " + e.getMessage());
        }
        if (user == null) {
            log.error("获取用户信息失败。");
            return null;
        }
        return user;
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return json字符串
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuilder buffer = new StringBuilder();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            httpUrlConn.disconnect();
            log.debug("https buffer:" + buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return buffer.toString();
    }

    /**
     * 将WechatUser里的信息转换为PersonInfo的信息并返回PersonInfo实体类
     *
     * @param wechatUser
     * @return
     */
    public static PersonInfo getPersonInfoFromRequest(WechatUser wechatUser) {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName(wechatUser.getNickName());
        personInfo.setGender(wechatUser.getSex() + "");
        personInfo.setProfileImg(wechatUser.getHeadimgurl());
        personInfo.setEnableStatus(1);
        return personInfo;
    }
}