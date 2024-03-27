package com.mvp.investservice.domain.exception;

public class SellUnavailableException extends RuntimeException {
    public SellUnavailableException(String message) {
        super(message);
    }
}
