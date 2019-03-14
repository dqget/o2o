package com.lovesickness.o2o.enums;

/**
 * @author 懿
 */
public enum UserAwardMapStateEnum {
    //用户与积分兑换奖品映射操作枚举类型
    OFFLINE(-1, "非法操作"),
    SUCCESS(0, "操作成功"),
    PASS(2, "通过认证"),
    INNER_ERROR(-1001, "操作失败"),
    EMPTY(-1002, "信息为空"),
    NULL_USER_AWARD_MAP(-1003, "兑换积分信息不完整");

    private int state;

    private String stateInfo;

    private UserAwardMapStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static UserAwardMapStateEnum stateOf(int index) {
        for (UserAwardMapStateEnum state : values()) {
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
