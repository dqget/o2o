package com.lovesickness.o2o.exception;

public class OrderOperationException extends RuntimeException {
    private static final long serialVersionUID = -3044996773819019091L;

    public OrderOperationException(String msg) {
        super(msg);
    }
}
