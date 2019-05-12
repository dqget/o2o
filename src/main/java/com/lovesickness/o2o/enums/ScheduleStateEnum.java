package com.lovesickness.o2o.enums;

/**
 * @author 懿
 */
public enum ScheduleStateEnum {
    //预定表操作枚举类型
    OFFLINE(-1, "非法预定"),
    SUCCESS(0, "操作成功"),
    PASS(2, "通过认证"),
    INNER_ERROR(-1001, "操作失败"),
    EMPTY(-1002, "信息为空");

    private int state;

    private String stateInfo;

    private ScheduleStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ScheduleStateEnum stateOf(int index) {
        for (ScheduleStateEnum state : values()) {
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
