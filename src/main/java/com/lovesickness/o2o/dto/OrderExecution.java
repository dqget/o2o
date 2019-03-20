package com.lovesickness.o2o.dto;

import com.lovesickness.o2o.entity.Order;
import com.lovesickness.o2o.enums.OrderStateEnum;

import java.util.List;

public class OrderExecution {
    // 结果状态
    private int state;

    // 状态标识
    private String stateInfo;

    private int count;

    private Order order;

    private List<Order> orderList;

    public OrderExecution() {
    }

    // 失败的构造器
    public OrderExecution(OrderStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    // 成功的构造器
    public OrderExecution(OrderStateEnum stateEnum, Order order) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.order = order;
    }

    // 成功的构造器
    public OrderExecution(OrderStateEnum stateEnum,
                              List<Order> orderList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.orderList = orderList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
