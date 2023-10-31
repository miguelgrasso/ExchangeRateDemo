package com.demo.exchangeRate.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExchangeRateAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new HashMap<>();
        Throwable throwable = super.getError(request);
        if(throwable instanceof ExchangeRateException) {
            ExchangeRateException exchangeRateException = (ExchangeRateException) throwable;
            errorAttributes.put("status", exchangeRateException.getStatus());
            errorAttributes.put("message", exchangeRateException.getMessage());
        }
        return errorAttributes;
    }
}
