package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.dto.OrderExecution;
import com.lovesickness.o2o.entity.*;
import com.lovesickness.o2o.enums.OrderStateEnum;
import com.lovesickness.o2o.service.OrderService;
import com.lovesickness.o2o.util.BuyerCartItemUtil;
import com.lovesickness.o2o.util.HttpServletRequestUtil;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private OrderService orderService;

    @PostMapping("/addorderbyuser")
    @ResponseBody
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

    @GetMapping("/getorderlistbyuser")
    @ApiOperation(value = "根据用户订单信息查询", notes = "查询用户的订单信息，status 0全部 1待付款、2待发货、3待收货 4待评价")
    public ResultBean<?> getOrderListByUser(String keyWord, int status, HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null || user.getUserId() == null) {
            return new ResultBean<>(false, 0, "请重新登录");
        }
        Integer pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        Integer pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if (pageIndex == null || pageSize == null) {
            return new ResultBean<>(false, 0, "empty pageIndex or pageSize");
        }

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

    private static class OrderAndItems {
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
