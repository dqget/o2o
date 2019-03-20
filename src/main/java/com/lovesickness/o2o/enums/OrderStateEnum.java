package com.lovesickness.o2o.enums;

public enum OrderStateEnum {
    //订单操作
    OFFLINE(-1, "非法订单"),
    SUCCESS(0, "操作成功"),
    PASS(2, "通过认证"),
    INNER_ERROR(-1001, "操作失败"),
    EMPTY(-1002, "订单为空"),
    EMPTY_MAP(-1003, "订单项为空");

    private int state;

    private String stateInfo;

    private OrderStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }


    public static OrderStateEnum stateOf(int index) {
        for (OrderStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
