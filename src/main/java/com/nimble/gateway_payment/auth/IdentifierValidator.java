package com.nimble.gateway_payment.auth;

public class IdentifierValidator {
    public IdentifierValidator(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Identifier is not empty.");
        }
    }
}
