package com.lovesickness.o2o.web.shopadmin;

import com.lovesickness.o2o.dto.OrderExecution;
import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.entity.OrderProductMap;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.entity.Shop;
import com.lovesickness.o2o.enums.OrderStateEnum;
import com.lovesickness.o2o.service.BuyerCartService;
import com.lovesickness.o2o.service.EvaluationService;
import com.lovesickness.o2o.service.OrderService;
import com.lovesickness.o2o.util.OrderUtil;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/shopadmin")
@Api(tags = "ShopOrderContorller|后台管理系统订单操作控制器")
public class ShopOrderContorller {

    private static Logger LOGGER = LoggerFactory.getLogger(ShopOrderContorller.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private BuyerCartService buyerCartService;
    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/getorderlistbyshop")
    @ApiOperation(value = "根据店铺订单信息查询",
            notes = "查询该店铺下的订单信息， state 0全部 1待付款、2待发货、3待收货 4待评价")
    public ResultBean<?> getOrderListByShop(@RequestParam("keyWord") String keyWord,
                                            @RequestParam("state") int state,
                                            @RequestParam("pageIndex") int pageIndex,
                                            @RequestParam("pageSize") int pageSize,
                                            HttpServletRequest request) {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (currentShop == null || currentShop.getShopId() == null) {
            return new ResultBean<>(false, 0, "请重新登录");
        }
        if (state == OrderUtil.ORDER_NOT_EVA) {
            return new ResultBean<>(orderService.getOrderNotEvaList(null, currentShop.getShopId(), keyWord, pageIndex, pageSize));
        }
        Order order = OrderUtil.compactOrderCondition(null, currentShop, state);
        return new ResultBean<>(orderService.getOrderList(order, keyWord, pageIndex, pageSize));
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


}
