package com.lovesickness.o2o.exception;

public class ProductOperationException extends RuntimeException {
    private static final long serialVersionUID = 8845767818972013520L;

    public ProductOperationException(String message) {
        super(message);
    }
}
