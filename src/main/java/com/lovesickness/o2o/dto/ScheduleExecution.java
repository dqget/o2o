package com.lovesickness.o2o.dto;

import com.lovesickness.o2o.entity.Schedule;
import com.lovesickness.o2o.enums.ScheduleStateEnum;

import java.util.List;

public class ScheduleExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    private int count;

    private Schedule schedule;

    private List<Schedule> scheduleList;

    public ScheduleExecution() {
    }

    //失败
    public ScheduleExecution(ScheduleStateEnum scheduleStateEnum) {
        this.state = scheduleStateEnum.getState();
        this.stateInfo = scheduleStateEnum.getStateInfo();
    }

    //成功
    public ScheduleExecution(ScheduleStateEnum scheduleStateEnum, Schedule schedule) {
        this.state = scheduleStateEnum.getState();
        this.stateInfo = scheduleStateEnum.getStateInfo();
        this.schedule = schedule;
    }

    public ScheduleExecution(ScheduleStateEnum scheduleStateEnum, List<Schedule> scheduleList) {
        this.state = scheduleStateEnum.getState();
        this.stateInfo = scheduleStateEnum.getStateInfo();
        this.scheduleList = scheduleList;
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


    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }
}
