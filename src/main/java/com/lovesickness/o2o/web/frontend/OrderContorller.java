package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.dto.OrderExecution;
import com.lovesickness.o2o.entity.BuyerCartItem;
import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.entity.OrderProductMap;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.enums.OrderStateEnum;
import com.lovesickness.o2o.service.OrderService;
import com.lovesickness.o2o.util.BuyerCartItemUtil;
import com.lovesickness.o2o.util.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/frontend")
public class OrderContorller {
    @Autowired
    private OrderService orderService;

    @PostMapping("/addorderbyuser")
    @ResponseBody
    public ResultBean<OrderExecution> addOrderByUser(@RequestBody OrderAndItems orderAndItems,HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        Order order = orderAndItems.getOrder();
        List<BuyerCartItem> items = orderAndItems.getItems();
        order.setUser(user);
        List<OrderProductMap> orderProductMapList = BuyerCartItemUtil.buyerCartItemList2OrderProductMapList(items);
        OrderExecution oe = orderService.addOrder(order, orderProductMapList);
        if (oe.getStateInfo().equals(OrderStateEnum.SUCCESS.getStateInfo())) {
            return new ResultBean<>(oe);
        } else {
            return new ResultBean<>(false, 0, oe.getStateInfo());
        }
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
