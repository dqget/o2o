package com.lovesickness.o2o.dto;

import com.lovesickness.o2o.entity.UserAwardMap;
import com.lovesickness.o2o.enums.AwardStateEnum;
import com.lovesickness.o2o.enums.UserAwardMapStateEnum;

import java.util.List;

public class UserAwardMapExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    private int count;

    private UserAwardMap userAwardMap;

    private List<UserAwardMap> userAwardMapList;

    public UserAwardMapExecution() {
    }

    //失败
    public UserAwardMapExecution(UserAwardMapStateEnum userAwardMapStateEnum) {
        this.state = userAwardMapStateEnum.getState();
        this.stateInfo = userAwardMapStateEnum.getStateInfo();
    }

    //成功
    public UserAwardMapExecution(UserAwardMapStateEnum userAwardMapStateEnum, UserAwardMap userAwardMap) {
        this.state = userAwardMapStateEnum.getState();
        this.stateInfo = userAwardMapStateEnum.getStateInfo();
        this.userAwardMap = userAwardMap;
    }

    public UserAwardMapExecution(AwardStateEnum awardStateEnum, List<UserAwardMap> userAwardMapList) {
        this.state = awardStateEnum.getState();
        this.stateInfo = awardStateEnum.getStateInfo();
        this.userAwardMapList = userAwardMapList;
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

    public UserAwardMap getUserAwardMap() {
        return userAwardMap;
    }

    public void setUserAwardMap(UserAwardMap userAwardMap) {
        this.userAwardMap = userAwardMap;
    }

    public List<UserAwardMap> getUserAwardMapList() {
        return userAwardMapList;
    }

    public void setUserAwardMapList(List<UserAwardMap> userAwardMapList) {
        this.userAwardMapList = userAwardMapList;
    }
}
