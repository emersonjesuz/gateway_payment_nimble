package com.nimble.gateway_payment.user.exceptions;

import com.nimble.gateway_payment.shared.exceptions.BadRequestException;

public class CpfCannotBeNullException extends BadRequestException {
    public CpfCannotBeNullException() {
        super("The CPF cannot be null.");
    }
}
