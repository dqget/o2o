package com.lovesickness.o2o.web.frontend;

import com.lovesickness.o2o.entity.BuyerCartItem;
import com.lovesickness.o2o.entity.PersonInfo;
import com.lovesickness.o2o.service.BuyerCartService;
import com.lovesickness.o2o.util.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/frontend")
@Api(tags = "BuyerCartController|购物车操作的控制器")
public class BuyerCartController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuyerCartController.class);
    @Autowired
    private BuyerCartService buyerCartService;

    @PostMapping("/updateitemtobuyercart")
    @ApiOperation(value = "修改购物车", notes = "增加、删除购物车里的商品")
    public ResultBean<?> updateItemToBuyerCart(@RequestBody BuyerCartItem buyerCartItem, HttpServletRequest request) {
        LOGGER.debug("添加购物车入参：" + buyerCartItem);
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResultBean<>(buyerCartService.updateItem(user.getUserId(), buyerCartItem));
        } else {
            return new ResultBean<>(false, 0, "用户信息不存在");
        }
    }

    @GetMapping("/getbuyercartbyuser")
    @ApiOperation(value = "查询购物车", notes = "根据用户查询购物车信息")
    public ResultBean<List<BuyerCartItem>> getBuyerCartByUser(HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResultBean<>(buyerCartService.getBuyerCart(user.getUserId()));
        } else {
            return new ResultBean<>(false, 0, "用户信息不存在");
        }
    }

    @GetMapping("/getbuyercartproductamountbyuser")
    @ApiOperation(value = "查询购物车商品总数", notes = "根据用户查询购物车商品总数")
    public ResultBean<Integer> getBuyerCartProductAmountByUser(HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResultBean<>(buyerCartService.getProductAmount(user.getUserId()));
        } else {
            return new ResultBean<>(false, 0, "用户信息不存在");
        }
    }

    @GetMapping("/getbuyercartpricebyuser")
    @ApiOperation(value = "查询购物车商品总折扣价格", notes = "根据用户查询购物车商品总折扣")
    public ResultBean<Integer> getBuyerCartPriceByUser(HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResultBean<>(buyerCartService.getProductPrice(user.getUserId()));
        } else {
            return new ResultBean<>(false, 0, "用户信息不存在");
        }
    }

    @GetMapping("/getbuyercartnormalpriceamountbyuser")
    @ApiOperation(value = "查询购物车商品总原价格", notes = "根据用户查询购物车商品总原价格")
    public ResultBean<Integer> getBuyerCartNormalPriceByUser(HttpServletRequest request) {
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResultBean<>(buyerCartService.getProductNormalPrice(user.getUserId()));
        } else {
            return new ResultBean<>(false, 0, "用户信息不存在");
        }
    }

}
