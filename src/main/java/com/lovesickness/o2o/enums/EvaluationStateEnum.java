package com.lovesickness.o2o.enums;

/**
 * @author 懿
 */
public enum EvaluationStateEnum {
    //商品评论枚举类型
    SUCCESS(0, "操作成功"),
    INNER_ERROR(-1001, "操作失败"),
    EMPTY(-1002, "信息为空");

    private int state;

    private String stateInfo;

    private EvaluationStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static EvaluationStateEnum stateOf(int index) {
        for (EvaluationStateEnum state : values()) {
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
