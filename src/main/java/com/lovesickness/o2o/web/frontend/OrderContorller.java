package com.lovesickness.o2o.web.frontend;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.lovesickness.o2o.config.alipay.ALiPayConfiguration;
import com.lovesickness.o2o.dto.OrderExecution;
import com.lovesickness.o2o.entity.*;
import com.lovesickness.o2o.enums.OrderStateEnum;
import com.lovesickness.o2o.service.BuyerCartService;
import com.lovesickness.o2o.service.EvaluationService;
import com.lovesickness.o2o.service.OrderService;
import com.lovesickness.o2o.util.BuyerCartItemUtil;
import com.lovesickness.o2o.util.OrderUtil;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/frontend")
@Api(tags = "OrderContorller|前端展示系统订单操作控制器")
public class OrderContorller {
    private static Logger LOGGER = LoggerFactory.getLogger(OrderContorller.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private BuyerCartService buyerCartService;
    @Autowired
    private EvaluationService evaluationService;

    @PostMapping("/addorderbyuser")
    @ApiOperation(value = "添加订单记录", notes = "根据订单项和session中存在的用户添加订单")
    public ResultBean<OrderExecution> addOrderByUser(@RequestBody OrderAndItems orderAndItems, HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        Order order = orderAndItems.getOrder();
        List<BuyerCartItem> items = orderAndItems.getItems();
        order.setUser(user);
        List<OrderProductMap> orderProductMapList = BuyerCartItemUtil.buyerCartItemList2OrderProductMapList(items);
        order.setShop(items.get(0).getProduct().getShop());
        OrderExecution oe = orderService.addOrder(order, orderProductMapList);
        if (oe.getStateInfo().equals(OrderStateEnum.SUCCESS.getStateInfo())) {
            return new ResultBean<>(oe);
        } else {
            return new ResultBean<>(false, 0, oe.getStateInfo());
        }
    }

    @PostMapping("/addorderandopenpay")
    @ApiOperation(value = "添加订单并根据用户订单信息使用支付宝支付功能",
            notes = "1.添加订单 2.删除购物车所选择的商品 3.唤起支付宝支付功能 4.支付成功后回调接口")
    public void aliPay(@RequestBody OrderAndItems orderAndItems, HttpServletRequest request, HttpServletResponse response) {
        //session中的用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null) {
            return;
        }
        //1.添加订单///
        Order order = orderAndItems.getOrder();

        List<BuyerCartItem> items = orderAndItems.getItems();
        order.setUser(user);
        //将购物车项转换为订单项
        List<OrderProductMap> orderProductMapList = BuyerCartItemUtil.buyerCartItemList2OrderProductMapList(items);
        order.setShop(items.get(0).getProduct().getShop());
        //添加订单 和 订单项
        OrderExecution oe = orderService.addOrder(order, orderProductMapList);
        if (!oe.getStateInfo().equals(OrderStateEnum.SUCCESS.getStateInfo())) {
            return;
        }
        //判断是否需要删除购物车中选中的商品
        //2.删除购物车所选择的商品///
        for (BuyerCartItem item : items) {
            item.setAmount(-item.getAmount());
        }
        boolean isSuccessClearBuyerCart = buyerCartService.updateItem(user.getUserId(), items);
        if (isSuccessClearBuyerCart) {
            //删除成功
            //3.唤起支付宝支付功能////
            aliPay(order, request, response);
        }

        //4.支付成功后回调接口///
    }

    @PostMapping("/oldorderopenpay")
    @ApiOperation(value = "根据已经存在的订单进行支付使用支付宝支付功能",
            notes = "1.查询订单判断是否支付 2.未支付唤起支付宝支付功能")
    public void oldOrderAliPay(@RequestBody JSONObject orderNoJSNO, HttpServletRequest request, HttpServletResponse response) {
        //session中的用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null) {
            return;
        }
        String orderNo = orderNoJSNO.getString("orderNo");
        Order order = orderService.getOrderByNo(orderNo);
        if (order.getIsPay() == 1) {
            return;
        }
        aliPay(order, request, response);

    }

    private void aliPay(Order order, HttpServletRequest request, HttpServletResponse response) {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(ALiPayConfiguration.gateWayUrl,
                ALiPayConfiguration.appId,
                ALiPayConfiguration.merchantPrivateKey,
                "json",
                ALiPayConfiguration.charset,
                ALiPayConfiguration.aliPublicKry,
                ALiPayConfiguration.signType);

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
            String subject = "订单名称";
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

    @GetMapping("/getorderlistbyuser")
    @ApiOperation(value = "根据用户订单信息查询", notes = "查询用户的订单信息，state 0全部 1待付款、2待发货、3待收货 4待评价")
    public ResultBean<?> getOrderListByUser(@RequestParam("keyWord") String keyWord,
                                            @RequestParam("state") int state,
                                            @RequestParam("pageIndex") int pageIndex,
                                            @RequestParam("pageSize") int pageSize,
                                            HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null || user.getUserId() == null) {
            return new ResultBean<>(false, 0, "请重新登录");
        }
        keyWord = keyWord.trim();
        if (state == OrderUtil.ORDER_NOT_EVA) {
            return new ResultBean<>(orderService.getOrderNotEvaList(user.getUserId(), null, keyWord, pageIndex, pageSize));
        }
        Order order = OrderUtil.compactOrderCondition(user, null, state);
        return new ResultBean<>(orderService.getOrderList(order, keyWord, pageIndex, pageSize));
    }


    @PostMapping("/modifyorderbyuser")
    @ApiOperation(value = "用户修改订单", notes = "用户修改订单信息，包括确认收货")
    public ResultBean<?> modifyOrderByUser(@RequestBody Order order, HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null || user.getUserId() == null) {
            return new ResultBean<>(false, 0, "请重新登录");
        }
        order.setUser(user);
        OrderExecution oe = orderService.modifyOrderByUser(order);
        if (oe.getState() == OrderStateEnum.SUCCESS.getState()) {
            return new ResultBean<>(true, 1, "修改成功");
        } else {
            return new ResultBean<>(false, 0, oe.getStateInfo());

        }
    }


    @GetMapping("/checkorderpaysuccess")
    @ApiOperation(value = "判断订单是否支付成功", notes = "轮询该接口查询是否成功")
    public int checkOrderPaySuccess(@RequestParam(value = "orderNo") String orderNo) {
        return orderService.getOrderByNo(orderNo).getIsPay();
    }

    @GetMapping("/getorderdetailbyno")
    @ResponseBody
    @ApiOperation(value = "查询订单详细信息", notes = "根据订单号查询订单信息")
    public ResultBean<Order> getOrderDetailByNo(@RequestParam(value = "orderNo") String orderNo, HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResultBean<>(orderService.getOrderByNo(orderNo));

        }
        return new ResultBean<>(false, 0, "请重新登录");
    }

    @GetMapping("/getorderproductmapbyid")
    @ResponseBody
    @ApiOperation(value = "查询订单项详细信息", notes = "根据订单项号查询订单信息")
    public ResultBean<OrderProductMap> getOrderProductMapById(@RequestParam(value = "orderProductMapId") Long orderProductMapId, HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResultBean<>(orderService.getOrderProductMapById(orderProductMapId));

        }
        return new ResultBean<>(false, 0, "请重新登录");
    }

    @PostMapping("/addevaandmodifyorderproductmap")
    @ResponseBody
    @ApiOperation(value = "添加一条评论并添加到对应的订单项", notes = "插入的评论相当于一穿评论的根评论")
    public ResultBean<?> addEvaAndModifyOrderProductMap(@RequestBody Evaluation evaluation, long orderProductMapId, long starLevel, HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null) {
            return new ResultBean<>(false, 0, "请重新登录");
        }
        evaluation.setFromUid(user.getUserId());
        evaluation.setFromName(user.getName());
        return new ResultBean<>(orderService.addEvaAndModifyOrderProductMap(orderProductMapId, starLevel, evaluation));
    }


    private static class OrderAndItems implements Serializable {
        private static final long serialVersionUID = 6382508072308942795L;
        Order order;
        List<BuyerCartItem> items;

        public OrderAndItems() {
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public List<BuyerCartItem> getItems() {
            return items;
        }

        public void setItems(List<BuyerCartItem> items) {
            this.items = items;
        }
    }
}
