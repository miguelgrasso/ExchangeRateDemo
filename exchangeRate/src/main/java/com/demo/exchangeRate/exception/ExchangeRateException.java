package com.demo.exchangeRate.exception;

import org.springframework.http.HttpStatus;

public class ExchangeRateException extends Exception{

    private HttpStatus status;

    public ExchangeRateException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}