package com.nimble.gateway_payment.auth.exception;

import com.nimble.gateway_payment.shared.exceptions.BadRequestException;

public class IdentifierIsEmptyException extends BadRequestException {
    public IdentifierIsEmptyException() {
        super("Identifier is not empty.");
    }
}
