package com.nimble.gateway_payment.user.exceptions;

import com.nimble.gateway_payment.shared.exceptions.BadRequestException;

public class CpfMustContainOnlyNumbersException extends BadRequestException {
    public CpfMustContainOnlyNumbersException() {
        super("The CPF must contain only numbers.");
    }
}
