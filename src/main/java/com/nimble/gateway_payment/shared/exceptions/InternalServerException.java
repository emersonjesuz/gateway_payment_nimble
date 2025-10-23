package com.nimble.gateway_payment.shared.exceptions;

import lombok.Getter;

@Getter
public class InternalServerException extends IllegalArgumentException {
    private int statusCode;
    private String message;

    public InternalServerException() {
        this.message = "Server error: account not found";
        this.statusCode = 500;
    }
}
