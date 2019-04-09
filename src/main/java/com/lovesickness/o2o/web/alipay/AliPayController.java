package com.lovesickness.o2o.web.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.lovesickness.o2o.config.alipay.ALiPayConfiguration;
import com.lovesickness.o2o.service.OrderService;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.IdGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝
 * 1.电脑网站支付产品alipay.trade.page.pay接口中，product_code为：FAST_INSTANT_TRADE_PAY
 * 2.手机网站支付产品alipay.trade.wap.pay接口中，product_code为：QUICK_WAP_WAY
 * 3.当面付条码支付产品alipay.trade.pay接口中，product_code为：FACE_TO_FACE_PAYMENT
 * 4.APP支付产品alipay.trade.app.pay接口中，product_code为：QUICK_MSECURITY_PAY
 *
 * @author 懿
 */
@Controller
@RequestMapping("/pay")
@Api(value = "AliPayController|负责支付请求的控制器")
public class AliPayController {
    private static Logger log = LoggerFactory.getLogger(AliPayController.class);
    @Autowired
    private OrderService orderService;

    @PostMapping("/open")
    @ApiOperation(value = "根据用户订单信息支付宝支付功能", notes = "测试--")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "WIDout_trade_no", value = "订单编号", required = true, dataType = "string"),
            @ApiImplicitParam(name = "WIDtotal_amount", value = "总金额", required = true, dataType = "string"),
            @ApiImplicitParam(name = "WIDsubject", value = "商品名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "WIDbody", value = "商品描述", required = true, dataType = "string")
    })
    public void aliPay(HttpServletRequest request, HttpServletResponse response) {
        log.debug("alipay支付功能接口！");
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(ALiPayConfiguration.gateWayUrl, ALiPayConfiguration.appId, ALiPayConfiguration.merchantPrivateKey, "json", ALiPayConfiguration.charset, ALiPayConfiguration.aliPublicKry, ALiPayConfiguration.signType);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(ALiPayConfiguration.returnUrl);
        alipayRequest.setNotifyUrl(ALiPayConfiguration.notifyUrl);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = null;
        try {
            out_trade_no = HttpServletRequestUtil.getString(request, "WIDout_trade_no");
            //付款金额，必填
            String total_amount = HttpServletRequestUtil.getString(request, "WIDtotal_amount");
            //订单名称，必填
            String subject = HttpServletRequestUtil.getString(request, "WIDsubject");
            //商品描述，可空
            String body = HttpServletRequestUtil.getString(request, "WIDbody");
            alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                    + "\"total_amount\":\"" + total_amount + "\","
                    + "\"subject\":\"" + subject + "\","
                    + "\"body\":\"" + body + "\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
            //请求
            String result = alipayClient.pageExecute(alipayRequest).getBody();

            response.setContentType("text/html;charset=utf-8");
            //输出
            PrintWriter writer = response.getWriter();
            //直接将完整的表单html输出到页面
            writer.println(result);
            writer.close();
        } catch (AlipayApiException | IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("notify")
    @ResponseBody
    public String notify(HttpServletRequest request) throws AlipayApiException {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号
        String out_trade_no = request.getParameter("out_trade_no");

        //支付宝交易号
        String trade_no = request.getParameter("trade_no");

        //交易状态
        String trade_status = request.getParameter("trade_status");
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, ALiPayConfiguration.aliPublicKry, "UTF-8", ALiPayConfiguration.signType);

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        if (signVerified) {
            //验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            boolean flg = false;
            if (trade_status.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序
                log.debug("该订单已经做过处理");
                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知

                //根据订单号将订单状态和支付宝记录表中状态都改为已支付
                log.debug("该订单未做过处理");
                flg = true;
            }

            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

            if (flg) {
                return "success";
            } else {
                return "fail";
            }
        } else {
            //验证失败
            log.debug("支付验签失败");
            return "fail";
        }
    }

    @GetMapping("/getorderno")
    @ResponseBody
    public String getOrderNo() {
        return IdGenerator.INSTANCE.nextId();
    }


}
