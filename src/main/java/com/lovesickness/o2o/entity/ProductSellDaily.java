package com.lovesickness.o2o.entity;

import java.util.Date;

/**
 * 消费商品记录
 *
 * @author 懿
 */
public class ProductSellDaily {
    private Integer productSellDailyId;
    /**
     * 哪天的销量,精确到天
     */
    private Date createTime;
    /**
     * 销量
     */
    private Integer total;

    private Product product;

    private Shop shop;

    public Integer getProductSellDailyId() {
        return productSellDailyId;
    }

    public void setProductSellDailyId(Integer productSellDailyId) {
        this.productSellDailyId = productSellDailyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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
        return "ProductSellDaily{" +
                "createTime=" + createTime +
                ", total=" + total +
                ", product=" + product +
                ", shop=" + shop +
                '}';
    }
}
