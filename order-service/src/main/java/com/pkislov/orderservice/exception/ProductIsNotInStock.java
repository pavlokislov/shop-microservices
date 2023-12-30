package com.pkislov.orderservice.exception;

public class ProductIsNotInStock extends RuntimeException {

    public ProductIsNotInStock(String message) {
        super(message);
    }
}
