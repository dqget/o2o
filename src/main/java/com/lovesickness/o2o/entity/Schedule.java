package com.lovesickness.o2o.entity;


import java.util.Date;
import java.util.List;

public class Schedule {

    private Long scheduleId;
    private PersonInfo user;
    private Shop shop;
    private Product product;
    private String productPrice;
    private Integer dailyQuantity;
    private Integer amountDay;
    private Integer isPay;
    private String payPrice;
    private Date payTime;
    private Date createTime;
    private Date updateTime;
    private Integer enableStatus;
    private List<ScheduleDistribution> scheduleDistributionList;

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", user=" + user +
                ", shop=" + shop +
                ", product=" + product +
                ", productPrice='" + productPrice + '\'' +
                ", dailyQuantity=" + dailyQuantity +
                ", amountDay=" + amountDay +
                ", isPay=" + isPay +
                ", payPrice='" + payPrice + '\'' +
                ", payTime=" + payTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enableStatus=" + enableStatus +
                ", scheduleDistributionList=" + scheduleDistributionList +
                '}';
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public PersonInfo getUser() {
        return user;
    }

    public void setUser(PersonInfo user) {
        this.user = user;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getDailyQuantity() {
        return dailyQuantity;
    }

    public void setDailyQuantity(Integer dailyQuantity) {
        this.dailyQuantity = dailyQuantity;
    }

    public Integer getAmountDay() {
        return amountDay;
    }

    public void setAmountDay(Integer amountDay) {
        this.amountDay = amountDay;
    }

    public Integer getIsPay() {
        return isPay;
    }

    public void setIsPay(Integer isPay) {
        this.isPay = isPay;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public List<ScheduleDistribution> getScheduleDistributionList() {
        return scheduleDistributionList;
    }

    public void setScheduleDistributionList(List<ScheduleDistribution> scheduleDistributionList) {
        this.scheduleDistributionList = scheduleDistributionList;
    }
}
