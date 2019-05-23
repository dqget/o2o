package com.lovesickness.o2o.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.lovesickness.o2o.config.alipay.ALiPayConfiguration;
import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.entity.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ALiPayUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(ALiPayUtil.class);

    private static AlipayClient getAliPayClient() {
        return new DefaultAlipayClient(ALiPayConfiguration.gateWayUrl,
                ALiPayConfiguration.appId,
                ALiPayConfiguration.merchantPrivateKey,
                "json",
                ALiPayConfiguration.charset,
                ALiPayConfiguration.aliPublicKry,
                ALiPayConfiguration.signType);
    }

    /**
     * 订单支付方法
     */
    public static void aliPay4Order(Order order, HttpServletRequest request, HttpServletResponse response) {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = getAliPayClient();

        //设置请求参数
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        alipayRequest.setReturnUrl(ALiPayConfiguration.returnUrl);
        alipayRequest.setNotifyUrl(ALiPayConfiguration.notifyUrl);

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        //商户订单号，商户网站订单系统中唯一订单号，必填
        String outTradeNo;
        try {
            outTradeNo = order.getOrderNumber();
            //付款金额，必填
            String totalAmount = order.getPayPrice();
            //订单名称，必填
            String subject = "AH的小花的订单";
            //商品描述，可空
            String body = "商品描述";
            //HttpServletRequestUtil.getString(request, "WIDbody");

            model.setOutTradeNo(outTradeNo);
            model.setSubject(subject);
            model.setTotalAmount(totalAmount);
            model.setBody(body);
            model.setProductCode("QUICK_WAP_WAY");
            alipayRequest.setBizModel(model);

            //请求
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            LOGGER.info(result);
            response.setContentType("text/html;charset=utf-8");
            //输出
            PrintWriter writer = response.getWriter();
            //直接将完整的表单html输出到页面
            writer.println(result);
            writer.flush();
            writer.close();
        } catch (AlipayApiException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 预定支付方法
     */
    public static void aliPay4Schedule(Schedule schedule, HttpServletRequest request, HttpServletResponse response) {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = getAliPayClient();

        //设置请求参数
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        alipayRequest.setReturnUrl(ALiPayConfiguration.returnUrl);

        alipayRequest.setNotifyUrl(ALiPayConfiguration.scheduleNotifyUrl);

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        //商户订单号，商户网站订单系统中唯一订单号，必填
        String outTradeNo;
        try {
            outTradeNo = schedule.getScheduleId().toString();
            //付款金额，必填
            String totalAmount = schedule.getPayPrice();
            //订单名称，必填
            String subject = schedule.getShop().getShopName() + "的订单";
            //商品描述，可空
            String body = "商品描述";
            //HttpServletRequestUtil.getString(request, "WIDbody");

            model.setOutTradeNo(outTradeNo);
            model.setSubject(subject);
            model.setTotalAmount(totalAmount);
            model.setBody(body);
            model.setProductCode("QUICK_WAP_WAY");
            alipayRequest.setBizModel(model);

            //请求
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            LOGGER.info(result);
            response.setContentType("text/html;charset=utf-8");
            //输出
            PrintWriter writer = response.getWriter();
            //直接将完整的表单html输出到页面
            writer.println(result);
            writer.flush();
            writer.close();
        } catch (AlipayApiException | IOException e) {
            e.printStackTrace();
        }
    }
}
