package com.nimble.gateway_payment.shared.exceptions;

import lombok.Getter;

@Getter
public class PaymentRequiredException extends RuntimeException {
    private int statusCode;
    private String message;

    public PaymentRequiredException(String message) {
        this.message = message;
        this.statusCode = 402;
    }
}
