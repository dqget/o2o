package com.lovesickness.o2o.dto;

import com.lovesickness.o2o.entity.UserShopMap;
import com.lovesickness.o2o.enums.UserShopMapStateEnum;

import java.util.List;

public class UserShopMapExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    private int count;

    private UserShopMap userShopMap;

    private List<UserShopMap> userShopMapList;

    public UserShopMapExecution() {
    }

    //失败
    public UserShopMapExecution(UserShopMapStateEnum userShopMapStateEnum) {
        this.state = userShopMapStateEnum.getState();
        this.stateInfo = userShopMapStateEnum.getStateInfo();
    }

    //成功
    public UserShopMapExecution(UserShopMapStateEnum userShopMapStateEnum, UserShopMap userShopMap) {
        this.state = userShopMapStateEnum.getState();
        this.stateInfo = userShopMapStateEnum.getStateInfo();
        this.userShopMap = userShopMap;
    }

    public UserShopMapExecution(UserShopMapStateEnum userShopMapStateEnum, List<UserShopMap> userShopMapList) {
        this.state = userShopMapStateEnum.getState();
        this.stateInfo = userShopMapStateEnum.getStateInfo();
        this.userShopMapList = userShopMapList;
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

    public UserShopMap getUserShopMap() {
        return userShopMap;
    }

    public void setUserShopMap(UserShopMap userShopMap) {
        this.userShopMap = userShopMap;
    }

    public List<UserShopMap> getUserShopMapList() {
        return userShopMapList;
    }

    public void setUserShopMapList(List<UserShopMap> userShopMapList) {
        this.userShopMapList = userShopMapList;
    }
}
