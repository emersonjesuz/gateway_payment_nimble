package com.nimble.gateway_payment.user.exceptions;

import com.nimble.gateway_payment.shared.exceptions.BadRequestException;

public class CpfInvalidException extends BadRequestException {
    public CpfInvalidException() {
        super("CPF inválid.");
    }
}
