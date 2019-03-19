package com.lovesickness.o2o.entity;

import java.io.Serializable;

/**
 * 购物车订单项
 */
public class BuyerCartItem implements Serializable {

    private static final long serialVersionUID = 4506865766629125198L;
    private Product product;
    private Integer amount;

    public BuyerCartItem() {
    }

    public BuyerCartItem(Product product, Integer amount) {
        this.product = product;
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BuyerCartItem that = (BuyerCartItem) o;

        return product.equals(that.product);
    }

    @Override
    public int hashCode() {
        return product.hashCode();
    }

    @Override
    public String toString() {
        return "BuyerCartItem{" +
                "product=" + product +
                ", amount=" + amount +
                '}';
    }
}
