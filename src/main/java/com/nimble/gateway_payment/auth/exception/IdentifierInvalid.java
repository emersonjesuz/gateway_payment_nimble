package com.nimble.gateway_payment.auth.exception;

import com.nimble.gateway_payment.shared.exceptions.BadRequestException;

public class IdentifierInvalid extends BadRequestException {
    public IdentifierInvalid() {
        super("Identifier invalid.");
    }
}
