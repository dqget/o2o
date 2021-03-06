package com.lovesickness.o2o.entity;

import java.util.Date;

/**
 * 商品评论对象
 */
public class Evaluation {
    /**
     * 主键自增
     */
    private Long evaluationId;
    /**
     * 评价的产品
     */
    private Product product;
    /**
     * 评价内容
     */
    private String content;
    /**
     * 发出评论的用户id
     */
    private Long fromUid;
    /**
     * 发出评论的用户昵称
     */
    private String fromName;
    /**
     * 被回复对象的用户id
     */
    private Long toUid;
    /**
     * 被回复对象的用户昵称
     */
    private String toName;

    private Date createTime;

    private Long starLevel;

    public Long getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(Long starLevel) {
        this.starLevel = starLevel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getFromUid() {
        return fromUid;
    }

    public void setFromUid(Long fromUid) {
        this.fromUid = fromUid;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public Long getToUid() {
        return toUid;
    }

    public void setToUid(Long toUid) {
        this.toUid = toUid;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "evaluationId=" + evaluationId +
                ", product=" + product +
                ", content='" + content + '\'' +
                ", fromUid=" + fromUid +
                ", fromName='" + fromName + '\'' +
                ", toUid=" + toUid +
                ", toName='" + toName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
