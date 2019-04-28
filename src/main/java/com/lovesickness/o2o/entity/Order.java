package com.lovesickness.o2o.entity;


import java.util.Date;
import java.util.List;

/**
 * 订单表
 */
public class Order {
    /**
     * 自增主键
     */
    private Long orderId;
    /**
     * 订单编号 工具生成 支付宝支付时需要
     */
    private String orderNumber;
    /**
     * 购买人
     */
    private PersonInfo user;
    /**
     * 支付价格
     */
    private String payPrice;
    /**
     * 是否支付 0未支付 1已支付
     */
    private Integer isPay;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 是否收货 0未 1已
     */
    private Integer isReceipt;
    /**
     * 收货时间
     */
    private Date receiptTime;
    /**
     * 是否发货 0未 1已
     */
    private Integer isShip;
    /**
     * 收货时间
     */
    private Date shipTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 该条记录状态 1正常 0禁用 -1删除
     */
    private Integer status;
    /**
     * 订单创建时间
     */
    private Date createTime;
    /**
     * 收货人手机号
     */
    private String receivePhone;
    /**
     * 收货人地址
     */
    private String receiveAddr;
    /**
     * 收货人姓名
     */
    private String receiveName;
    /**
     * 快递单号
     */
    private String trackNumber;
    private List<OrderProductMap> orderProductMapList;

    /**
     * 店铺
     */
    private Shop shop;

    public Order() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public PersonInfo getUser() {
        return user;
    }

    public void setUser(PersonInfo user) {
        this.user = user;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public Integer getIsPay() {
        return isPay;
    }

    public void setIsPay(Integer isPay) {
        this.isPay = isPay;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getIsReceipt() {
        return isReceipt;
    }

    public void setIsReceipt(Integer isReceipt) {
        this.isReceipt = isReceipt;
    }


    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    public Integer getIsShip() {
        return isShip;
    }

    public void setIsShip(Integer isShip) {
        this.isShip = isShip;
    }

    public Date getShipTime() {
        return shipTime;
    }

    public void setShipTime(Date shipTime) {
        this.shipTime = shipTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public List<OrderProductMap> getOrderProductMapList() {
        return orderProductMapList;
    }

    public void setOrderProductMapList(List<OrderProductMap> orderProductMapList) {
        this.orderProductMapList = orderProductMapList;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", user=" + user +
                ", payPrice='" + payPrice + '\'' +
                ", isPay=" + isPay +
                ", payTime=" + payTime +
                ", isReceipt=" + isReceipt +
                ", receiptTime=" + receiptTime +
                ", isShip=" + isShip +
                ", shipTime=" + shipTime +
                ", updateTime=" + updateTime +
                ", status=" + status +
                ", createTime=" + createTime +
                ", receivePhone='" + receivePhone + '\'' +
                ", receiveAddr='" + receiveAddr + '\'' +
                ", receiveName='" + receiveName + '\'' +
                ", trackNumber='" + trackNumber + '\'' +
                ", orderProductMapList=" + orderProductMapList +
                ", shop=" + shop +
                '}';
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

}
