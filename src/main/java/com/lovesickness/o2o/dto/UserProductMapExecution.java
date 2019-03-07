package com.lovesickness.o2o.dto;

import com.lovesickness.o2o.entity.UserProductMap;
import com.lovesickness.o2o.enums.UserProductMapStateEnum;

import java.util.List;

/**
 * @author 懿
 */
public class UserProductMapExecution {
    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    private List<UserProductMap> userProductMapList;
    private int count;

    public UserProductMapExecution() {
    }

    /**
     * 失败的构造方法
     */
    public UserProductMapExecution(UserProductMapStateEnum userProductMapStateEnum) {
        this.state = userProductMapStateEnum.getState();
        this.stateInfo = userProductMapStateEnum.getStateInfo();
    }

    /**
     * 成功的构造方法
     */
    public UserProductMapExecution(UserProductMapStateEnum userProductMapStateEnum, List<UserProductMap> userProductMapList, int count) {
        this.state = userProductMapStateEnum.getState();
        this.stateInfo = userProductMapStateEnum.getStateInfo();
        this.userProductMapList = userProductMapList;
        this.count = count;
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

    public List<UserProductMap> getUserProductMapList() {
        return userProductMapList;
    }

    public void setUserProductMapList(List<UserProductMap> userProductMapList) {
        this.userProductMapList = userProductMapList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
