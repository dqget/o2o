package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.entity.BuyerCartItem;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.service.BuyerCartService;
import com.lovesickness.o2o.util.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/frontend")
public class BuyerCartController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuyerCartController.class);
    @Autowired
    private BuyerCartService buyerCartService;
    @PostMapping("/updateitemtobuyercart")

    public ResultBean<Boolean> updateItemToBuyerCart(@RequestBody BuyerCartItem buyerCartItem, HttpServletRequest request) {
        LOGGER.debug("添加购物车入参：" + buyerCartItem);
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResultBean<>(buyerCartService.updateItem(user.getUserId(), buyerCartItem));
        } else {
            return new ResultBean<>(false, 0, "用户信息不存在");
        }
    }

    @GetMapping("/getbuyercartbyuser")
    public ResultBean<List<BuyerCartItem>> getBuyerCartByUser(HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResultBean<>(buyerCartService.getBuyerCart(user.getUserId()));
        } else {
            return new ResultBean<>(false, 0, "用户信息不存在");
        }
    }

    @GetMapping("/getbuyercartproductamountbyuser")
    public ResultBean<Integer> getBuyerCartProductAmountByUser(HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResultBean<>(buyerCartService.getProductAmount(user.getUserId()));
        } else {
            return new ResultBean<>(false, 0, "用户信息不存在");
        }
    }

    @GetMapping("/getbuyercartpricebyuser")
    public ResultBean<Integer> getBuyerCartPriceByUser(HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResultBean<>(buyerCartService.getProductPrice(user.getUserId()));
        } else {
            return new ResultBean<>(false, 0, "用户信息不存在");
        }
    }

    @GetMapping("/getbuyercartnormalpriceamountbyuser")
    public ResultBean<Integer> getBuyerCartNormalPriceByUser(HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResultBean<>(buyerCartService.getProductNormalPrice(user.getUserId()));
        } else {
            return new ResultBean<>(false, 0, "用户信息不存在");
        }
    }
}
