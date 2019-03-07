package com.lovesickness.o2o.entity;

import java.util.Date;

/**
 * 顾客与购买商品映射
 *
 * @author 懿
 */
public class UserProductMap {
    private Long userProductId;
    private Date createTime;
    /**
     * 购买商品所得积分
     */
    private Integer point;
    private PersonInfo user;
    private Product product;
    private Shop shop;
    /**
     * 操作员工
     */
    private PersonInfo operator;

    public PersonInfo getOperator() {
        return operator;
    }

    public void setOperator(PersonInfo operator) {
        this.operator = operator;
    }


    public Long getUserProductId() {
        return userProductId;
    }

    public void setUserProductId(Long userProductId) {
        this.userProductId = userProductId;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public String toString() {
        return "UserProductMap{" +
                "userProductId=" + userProductId +
                ", createTime=" + createTime +
                ", point=" + point +
                ", user=" + user +
                ", product=" + product +
                ", shop=" + shop +
                ", operator=" + operator +
                '}';
    }
}
