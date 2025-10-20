package com.nimble.gateway_payment.user.exceptions;

import com.nimble.gateway_payment.shared.exceptions.BadRequestException;

public class CpfMustContainExactly11NumbersException extends BadRequestException {
    public CpfMustContainExactly11NumbersException() {
        super("The CPF must contain exactly 11 numeric digits.");
    }
}
