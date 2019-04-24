package com.lovesickness.o2o.web.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.lovesickness.o2o.config.alipay.ALiPayConfiguration;
import com.lovesickness.o2o.dto.OrderExecution;
import com.lovesickness.o2o.dto.UserProductMapExecution;
import com.lovesickness.o2o.dto.UserShopMapExecution;
import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.entity.UserProductMap;
import com.lovesickness.o2o.entity.UserShopMap;
import com.lovesickness.o2o.enums.OrderStateEnum;
import com.lovesickness.o2o.enums.UserProductMapStateEnum;
import com.lovesickness.o2o.enums.UserShopMapStateEnum;
import com.lovesickness.o2o.service.OrderService;
import com.lovesickness.o2o.service.UserProductMapService;
import com.lovesickness.o2o.service.UserShopMapService;
import com.lovesickness.o2o.util.EntityTransformation;
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
import java.util.*;

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
@Api(tags = "AliPayController|负责支付请求的控制器")
public class AliPayController {
    private static Logger log = LoggerFactory.getLogger(AliPayController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private UserShopMapService userShopMapService;

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
        String outTradeNo;
        try {
            outTradeNo = HttpServletRequestUtil.getString(request, "WIDout_trade_no");
            //付款金额，必填
            String totalAmount = HttpServletRequestUtil.getString(request, "WIDtotal_amount");
            //订单名称，必填
            String subject = HttpServletRequestUtil.getString(request, "WIDsubject");
            //商品描述，可空
            String body = HttpServletRequestUtil.getString(request, "WIDbody");
            alipayRequest.setBizContent("{\"out_trade_no\":\"" + outTradeNo + "\","
                    + "\"total_amount\":\"" + totalAmount + "\","
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

    @PostMapping("/notify")
    @ResponseBody
    @ApiOperation(value = "支付异步接口", notes = "开发未完成")
    public String notify(HttpServletRequest request) throws AlipayApiException {
        log.info("支付宝异步接口调用成功");
        //获取支付宝POST过来反馈信息
        Map<String, String> params = getAliBaBaReturnMap(request);
        log.debug(String.valueOf(params));
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号
        String outTradeNo = request.getParameter("out_trade_no");
        //支付宝交易号
        String tradeNo = request.getParameter("trade_no");
        //交易状态
        String tradeStatus = request.getParameter("trade_status");
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params,
                ALiPayConfiguration.aliPublicKry,
                "UTF-8",
                ALiPayConfiguration.signType);
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        if (signVerified) {
            //验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            boolean flg = false;
            if ("TRADE_FINISHED".equals(tradeStatus)) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序
                log.debug("该订单已经做过处理");
                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if ("TRADE_SUCCESS".equals(tradeStatus)) {
                //判断该笔订单是否在商户网站中已经做过处理
                log.debug("该订单未做过处理");
                //如果没有做过处理，根据订单号（out_tr_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //1.修改订单信息
                Order order = orderService.getOrderByNo(outTradeNo);
                order.setIsPay(1);
                order.setPayTime(new Date());
                OrderExecution oe = orderService.modifyOrderByUser(order);
                if (oe.getState() == OrderStateEnum.SUCCESS.getState()) {
                    //2.订单中的订单项  转化为购买记录
                    List<UserProductMap> userProductMaps =
                            EntityTransformation.order2UserProductMaps(order);
                    //3.添加用户购买记录
                    UserProductMapExecution upme = userProductMapService
                            .batchAddUserProductMap(userProductMaps);
                    if (upme.getState() == UserProductMapStateEnum.SUCCESS.getState()) {
                        //4.添加用户积分
                        UserShopMap userShopMap = new UserShopMap();
                        userShopMap.setCreateTime(new Date());
                        userShopMap.setUser(order.getUser());
                        userShopMap.setShop(order.getShop());
                        int pointCount = userProductMaps
                                .stream()
                                .mapToInt(UserProductMap::getPoint)
                                .sum();
                        userShopMap.setPoint(pointCount);
                        UserShopMapExecution usme = userShopMapService.addUserShopMap(userShopMap);
                        if (usme.getState() == UserShopMapStateEnum.SUCCESS.getState()) {
                            flg = true;
                        } else {
                            log.error("支付成功，添加用户积分失败");
                        }
                    } else {
                        log.error("支付成功，添加用户购买记录失败");
                    }
                } else {
                    log.error("支付成功，修改订单失败");

                }
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知

                //根据订单号将订单状态和支付宝记录表中状态都改为已支付
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


    @GetMapping("/return")
    public String returnPay(HttpServletRequest request, HttpServletResponse response) {
        log.info("支付宝回调接口调用成功");
        //获取支付宝GET过来反馈信息
        Map<String, String> params = getAliBaBaReturnMap(request);

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String outTradeNo = HttpServletRequestUtil.getString(request, "out_trade_no");

        //支付宝交易号

        String tradeNo = HttpServletRequestUtil.getString(request, "trade_no");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean verifyResult = false;
        try {
            verifyResult = AlipaySignature.rsaCheckV1(params,
                    ALiPayConfiguration.aliPublicKry,
                    ALiPayConfiguration.charset,
                    ALiPayConfiguration.signType);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        try {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            if (verifyResult) {
                //验证成功
                //////////////////////////////////////////////////////////////////////////////////////////
                //请在这里加上商户的业务逻辑程序代码
                //该页面可做页面美工编辑


                out.println("验证成功<br/>请回到微信<br/>");
                out.close();
                //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
                return "frontend/index";

                //////////////////////////////////////////////////////////////////////////////////////////
            } else {
                //该页面可做页面美工编辑
                log.info("验证失败");

                out.println("验证失败");
                out.close();
                return "local/login";

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @GetMapping("/getorderno")
    @ResponseBody
    public String getOrderNo() {
        return IdGenerator.INSTANCE.nextId();
    }

    /**
     * 获取支付宝过来反馈信息
     *
     * @param request
     * @return
     */
    private Map<String, String> getAliBaBaReturnMap(HttpServletRequest request) {

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
        return params;
    }
}
