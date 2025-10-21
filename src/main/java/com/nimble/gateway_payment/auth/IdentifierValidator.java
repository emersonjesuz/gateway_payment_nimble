package com.nimble.gateway_payment.auth;

import com.nimble.gateway_payment.auth.exception.IdentifierInvalid;
import com.nimble.gateway_payment.auth.exception.IdentifierIsEmptyException;
import lombok.Getter;

@Getter
public class IdentifierValidator {
    private String email;
    private String cpf;

    public IdentifierValidator(String value) {
        if (value == null || value.isEmpty()) {
            throw new IdentifierIsEmptyException();
        }

        if (value.contains("@") && value.contains(".")) {
            this.email = value;
            return;
        }

        if (value.matches("\\d{11}")) {
            this.cpf = value;
            return;
        }
        throw new IdentifierInvalid();
    }

}
