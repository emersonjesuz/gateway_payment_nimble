package com.nimble.gateway_payment.shared.exceptions;

import lombok.Getter;

@Getter
public class BadRequestException extends IllegalArgumentException {
    private int statusCode;
    private String message;

    public BadRequestException(String message) {
        this.message = message;
        this.statusCode = 400;
    }
}
