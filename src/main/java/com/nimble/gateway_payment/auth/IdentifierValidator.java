package com.nimble.gateway_payment.auth;

import lombok.Getter;

@Getter
public class IdentifierValidator {
    private String email;

    public IdentifierValidator(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Identifier is not empty.");
        }

        if (value.contains("@") && value.contains(".")) {
            this.email = value;
            return;
        }
    }

}
