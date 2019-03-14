package com.lovesickness.o2o.entity;

import java.util.Date;

/**
 * 顾客与店铺积分映射
 */
public class UserShopMap {
    private Long userShopId;

    private Date createTime;
    /**
     * 在该店铺的总积分
     */
    private Integer point;
    private PersonInfo user;
    private Shop shop;

    public Long getUserShopId() {
        return userShopId;
    }

    public void setUserShopId(Long userShopId) {
        this.userShopId = userShopId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public String toString() {
        return "UserShopMap{" +
                "userShopId=" + userShopId +
                ", createTime=" + createTime +
                ", point=" + point +
                ", user=" + user +
                ", shop=" + shop +
                '}';
    }
}
