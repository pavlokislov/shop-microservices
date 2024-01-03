package com.pkislov.inventoryservice.exeception;

public class DuplicatedSkuCodeException extends RuntimeException {

    public DuplicatedSkuCodeException(String message) {
        super(message);
    }
}
