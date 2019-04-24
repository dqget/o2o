package com.lovesickness.o2o.web.frontend;

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
import com.lovesickness.o2o.service.OrderService;
import com.lovesickness.o2o.util.BuyerCartItemUtil;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import com.lovesickness.o2o.web.alipay.AliPayController;
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
@Api(tags = "OrderContorller|订单操作控制器")
public class OrderContorller {
    private static final int ORDER_ALL = 0;
    private static final int ORDER_NOT_PAY = 1;
    private static final int ORDER_NOT_SHIP = 2;
    private static final int ORDER_NOT_RECEIPT = 3;
    private static final int ORDER_NOT_EVA = 4;
    private static Logger LOGGER = LoggerFactory.getLogger(AliPayController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private BuyerCartService buyerCartService;

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
    @ApiOperation(value = "添加订单并根据用户订单信息使用支付宝支付功能", notes = "测试--")
    public void aliPay(@RequestBody OrderAndItems orderAndItems, HttpServletRequest request, HttpServletResponse response) {
        //session中的用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null) {
            return;
        }
        //1.添加订单
        Order order = orderAndItems.getOrder();
        List<BuyerCartItem> items = orderAndItems.getItems();
        order.setUser(user);
        List<OrderProductMap> orderProductMapList = BuyerCartItemUtil.buyerCartItemList2OrderProductMapList(items);
        order.setShop(items.get(0).getProduct().getShop());
        OrderExecution oe = orderService.addOrder(order, orderProductMapList);
        if (!oe.getStateInfo().equals(OrderStateEnum.SUCCESS.getStateInfo())) {
            return;
        }
        //2.删除购物车所选择的商品
        for (BuyerCartItem item : items) {
            item.setAmount(-item.getAmount());
        }
        boolean isClearBuyerCart = buyerCartService.updateItem(user.getUserId(), items);
        if (isClearBuyerCart) {
            //3.唤起支付宝支付功能
            aliPay(order, request, response);
        }
        //4.支付成功后回调接口
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
    @ApiOperation(value = "根据用户订单信息查询", notes = "查询用户的订单信息，status 0全部 1待付款、2待发货、3待收货 4待评价")
    public ResultBean<?> getOrderListByUser(String keyWord, int status, int pageIndex, int pageSize, HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null || user.getUserId() == null) {
            return new ResultBean<>(false, 0, "请重新登录");
        }
//        Integer pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
//        Integer pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
//        if (pageIndex == null || pageSize == null) {
//            return new ResultBean<>(false, 0, "empty pageIndex or pageSize");
//        }

        if (status == ORDER_NOT_EVA) {
            return new ResultBean<>(orderService.getOrderNotEvaList(user.getUserId(), null, keyWord, pageIndex, pageSize));
        }
        Order order = compactOrderCondition(user, null, status);
        return new ResultBean<>(orderService.getOrderList(order, keyWord, pageIndex, pageSize));
    }

    @GetMapping("/getorderlistbyshop")
    @ApiOperation(value = "根据店铺订单信息查询", notes = "查询该店铺下的订单信息， status 0全部 1待付款、2待发货、3待收货 4待评价")
    public ResultBean<?> getOrderListByShop(String keyWord, int status, HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        Integer pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        Integer pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if (pageIndex == null || pageSize == null) {
            return new ResultBean<>(false, 0, "empty pageIndex or pageSize");
        }
        if (status == ORDER_NOT_EVA) {
            return new ResultBean<>(orderService.getOrderNotEvaList(null, currentShop.getShopId(), keyWord, pageIndex, pageSize));
        }
        Order order = compactOrderCondition(null, currentShop, status);
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

    @PostMapping("/modifyorderbyshop")
    @ApiOperation(value = "店家修改订单", notes = "店家修改订单信息，包括发货")
    public ResultBean<?> modifyOrderByShop(@RequestBody Order order, HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (currentShop == null || currentShop.getShopId() == null) {
            return new ResultBean<>(false, 0, "请重新登录");
        }
        order.setShop(currentShop);
        OrderExecution oe = orderService.modifyOrderByShop(order);
        if (oe.getState() == OrderStateEnum.SUCCESS.getState()) {
            return new ResultBean<>(true, 1, "修改成功");
        } else {
            return new ResultBean<>(false, 0, oe.getStateInfo());

        }
    }

    /**
     * @param status 0全部 1待付款、2待发货、3待收货 4待评价
     */
    private Order compactOrderCondition(PersonInfo user, Shop shop, int status) {
        Order order = new Order();
        order.setUser(user);
        order.setShop(shop);
        switch (status) {
            case ORDER_ALL:
                break;
            case ORDER_NOT_PAY:
                order.setIsPay(0);
                order.setIsShip(0);
                order.setIsReceipt(0);
                break;
            case ORDER_NOT_SHIP:
                order.setIsPay(1);
                order.setIsShip(0);
                order.setIsReceipt(0);
                break;
            case ORDER_NOT_RECEIPT:
                order.setIsPay(1);
                order.setIsShip(1);
                order.setIsReceipt(0);
                break;
            default:
                break;
        }
        return order;
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
