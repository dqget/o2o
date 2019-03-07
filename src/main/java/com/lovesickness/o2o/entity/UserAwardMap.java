package com.lovesickness.o2o.entity;

import java.util.Date;

/**
 * 顾客与所兑换奖品的映射
 *
 * @author 懿
 */
public class UserAwardMap {

    private Long userAwardId;
    private Date createTime;
    /**
     * 0未兑换1已兑换
     */
    private Integer usedStatus;
    /**
     * 积分
     */
    private Integer point;
    private PersonInfo user;
    private Award award;
    private Shop shop;
    /**
     * 操作员
     */
    private PersonInfo operator;

    public PersonInfo getOperator() {
        return operator;
    }

    public void setOperator(PersonInfo operator) {
        this.operator = operator;
    }

    public Long getUserAwardId() {
        return userAwardId;
    }

    public void setUserAwardId(Long userAwardId) {
        this.userAwardId = userAwardId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUsedStatus() {
        return usedStatus;
    }

    public void setUsedStatus(Integer usedStatus) {
        this.usedStatus = usedStatus;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public PersonInfo getUser() {
        return user;
    }

    public void setUser(PersonInfo user) {
        this.user = user;
    }

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public String toString() {
        return "UserAwardMap{" +
                "userAwardId=" + userAwardId +
                ", createTime=" + createTime +
                ", usedStatus=" + usedStatus +
                ", point=" + point +
                ", user=" + user +
                ", award=" + award +
                ", shop=" + shop +
                ", operator=" + operator +
                '}';
    }
}
