package com.lovesickness.o2o.enums;

public enum ShopAuthStateEnum {
    SUCCESS(0, "操作成功"), INNER_ERROR(-1001,
            "操作失败"), NULL_SHOPAUTH_ID(-1007, "ShopAuthId为空"), NULL_SHOPAUTH_INFO(-1003, "传入了空的信息");

    private int state;

    private String stateInfo;

    private ShopAuthStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ShopAuthStateEnum stateOf(int index) {
        for (ShopAuthStateEnum state : values()) {
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
