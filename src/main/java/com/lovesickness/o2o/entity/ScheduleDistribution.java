package com.lovesickness.o2o.entity;


import java.util.Date;


public class ScheduleDistribution {

    private Long scheduleDistributionId;
    private Long scheduleId;
    private PersonInfo operator;
    private String receiveName;
    private String receivePhone;
    private String receiveAddr;
    private Date receiptTime;
    private Integer isReceipt;
    private Date updateTime;

    public Long getScheduleDistributionId() {
        return scheduleDistributionId;
    }

    public void setScheduleDistributionId(Long scheduleDistributionId) {
        this.scheduleDistributionId = scheduleDistributionId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public PersonInfo getOperator() {
        return operator;
    }

    public void setOperator(PersonInfo operator) {
        this.operator = operator;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getReceiveAddr() {
        return receiveAddr;
    }

    public void setReceiveAddr(String receiveAddr) {
        this.receiveAddr = receiveAddr;
    }

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    public Integer getIsReceipt() {
        return isReceipt;
    }

    public void setIsReceipt(Integer isReceipt) {
        this.isReceipt = isReceipt;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ScheduleDistribution{" +
                "scheduleDistributionId=" + scheduleDistributionId +
                ", scheduleId=" + scheduleId +
                ", operator=" + operator +
                ", receiveName='" + receiveName + '\'' +
                ", receivePhone='" + receivePhone + '\'' +
                ", receiveAddr='" + receiveAddr + '\'' +
                ", receiptTime=" + receiptTime +
                ", isReceipt=" + isReceipt +
                ", updateTime=" + updateTime +
                '}';
    }
}

