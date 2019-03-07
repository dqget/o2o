package com.lovesickness.o2o.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author 懿
 */
public class ShopNetAddressUtil {
    final static String TOKEN = "7952cab9b51271b7c4e54d8f5b3cca02";
    private static int TIME_OUT = 30 * 1000;
    private static String ENCODING = "UTF-8";
    private static Logger LOGGER = LoggerFactory.getLogger(ShopNetAddressUtil.class);
    private static String BaiDuShortUrl_API = "https://dwz.cn/admin/v2/create";

    public static void main(String[] args) {
        getShortURL("https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login");

    }

    public static String getShortURL(String originURL) {
        String tinyUrl = null;
        String params = "{\"url\":\"" + originURL + "\"}";
        try {
            URL url = new URL(BaiDuShortUrl_API);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //使用连接进行输出
            connection.setDoOutput(true);
            //使用连接进行输入
            connection.setDoInput(true);
            //不使用缓存
            connection.setUseCaches(false);
            //设置超时时间
            connection.setConnectTimeout(TIME_OUT);
            //请求方式  13661289043
            connection.setRequestMethod("POST");
            // 设置发送数据的格式
            connection.setRequestProperty("Content-Type", "application/json");
            // 设置发送数据的格式");
            connection.setRequestProperty("Token", TOKEN);
            //连接百度短视频接口
            connection.connect();
            // utf-8编码
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), ENCODING);
            out.append(params);
            out.flush();
            out.close();

            //获取返回的字符串
            String responseStr = getResponse(connection);
            LOGGER.info("response string=" + responseStr);
            //在字符串里获取短视频地址
            tinyUrl = getValueByKey(responseStr, "ShortUrl");
            LOGGER.info("ShortUrl: " + tinyUrl);
            //关闭连接
            connection.disconnect();


        } catch (IOException e) {
            LOGGER.error("getShortURL error: " + e.getMessage());
            e.printStackTrace();
        }
        return tinyUrl;
    }

    /**
     * JSON 依据传入的key获取value
     *
     * @param replyText
     * @param key
     * @return
     */
    private static String getValueByKey(String replyText, String key) {
        ObjectMapper mapper = new ObjectMapper();
        //定义json节点
        JsonNode node;
        String targetValue = null;
        try {
            node = mapper.readTree(replyText);
            targetValue = node.get(key).asText();
        } catch (IOException e) {
            LOGGER.error("getValueByKey error: " + e.getMessage());
            e.printStackTrace();
        }
        return targetValue;
    }

    /**
     * 获取HttpURLConnection中的字符串
     *
     * @param connection
     * @return
     */
    private static String getResponse(HttpURLConnection connection) {
        StringBuilder result = new StringBuilder();
        //连接状态码
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //如果连接是OK的取出连接的输入流
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), ENCODING));
                String inputLine = "";
                while ((inputLine = reader.readLine()) != null) {
                    //将消息逐行读入结果
                    result.append(inputLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    class UrlResponse {
        private int code;

        private String errMsg;

        private String longUrl;

        private String shortUrl;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public String getLongUrl() {
            return longUrl;
        }

        public void setLongUrl(String longUrl) {
            this.longUrl = longUrl;
        }

        public String getShortUrl() {
            return shortUrl;
        }

        public void setShortUrl(String shortUrl) {
            this.shortUrl = shortUrl;
        }
    }
}
