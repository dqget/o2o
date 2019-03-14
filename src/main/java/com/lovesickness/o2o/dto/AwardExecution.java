package com.lovesickness.o2o.dto;

import com.lovesickness.o2o.entity.Award;
import com.lovesickness.o2o.enums.AwardStateEnum;

import java.util.List;

public class AwardExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    private int count;

    private Award award;

    private List<Award> awardList;

    public AwardExecution() {
    }

    //失败
    public AwardExecution(AwardStateEnum awardStateEnum) {
        this.state = awardStateEnum.getState();
        this.stateInfo = awardStateEnum.getStateInfo();
    }

    //成功
    public AwardExecution(AwardStateEnum awardStateEnum, Award award) {
        this.state = awardStateEnum.getState();
        this.stateInfo = awardStateEnum.getStateInfo();
        this.award = award;
    }

    public AwardExecution(AwardStateEnum awardStateEnum, List<Award> awardList) {
        this.state = awardStateEnum.getState();
        this.stateInfo = awardStateEnum.getStateInfo();
        this.awardList = awardList;
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

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public List<Award> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<Award> awardList) {
        this.awardList = awardList;
    }
}
