package com.nimble.gateway_payment.auth.dtos.exception;

import com.nimble.gateway_payment.shared.exceptions.BadRequestException;

public class IdentifierOrPasswordIncorrectException extends BadRequestException {
    public IdentifierOrPasswordIncorrectException() {
        super("Identifier or password incorrect.");
    }
}
