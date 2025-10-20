package com.nimble.gateway_payment.shared.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends IllegalArgumentException {
    private int statusCode;
    private String message;

    public NotFoundException(String message) {
        this.message = message;
        this.statusCode = 404;
    }
}
