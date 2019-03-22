package com.lovesickness.o2o.entity;


import java.util.Date;

public class OrderProductMap {
    /**
     * 主键自增
     */
    private Long orderProductId;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 购买的产品
     */
    private Product product;
    /**
     * 购买的产品数量
     */
    private Integer productNum;
    /**
     * 产品单价
     */
    private String productPrice;
    /**
     * 是否评价 0未评价 1已评价
     */
    private Long isEvaluation;
    /**
     * 评价星级
     */
    private Long starLevel;
    /**
     *
     */
    private Long evaluationId;
    /**
     * 该条记录状态 1正常 0禁用 -1删除
     */
    private Integer status;

    private Date createTime;
    private Date updateTime;

    public Long getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Long orderProductId) {
        this.orderProductId = orderProductId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getProductNum() {
        return productNum;
    }

    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Long getIsEvaluation() {
        return isEvaluation;
    }

    public void setIsEvaluation(Long isEvaluation) {
        this.isEvaluation = isEvaluation;
    }

    public Long getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(Long starLevel) {
        this.starLevel = starLevel;
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "OrderProductMap{" +
                "orderProductId=" + orderProductId +
                ", orderId=" + orderId +
                ", product=" + product +
                ", productNum=" + productNum +
                ", productPrice='" + productPrice + '\'' +
                ", isEvaluation=" + isEvaluation +
                ", starLevel=" + starLevel +
                ", evaluationId=" + evaluationId +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
