package com.nimble.gateway_payment.charges.exception;

import com.nimble.gateway_payment.shared.exceptions.BadRequestException;

public class OriginatorEqualsRecipientException extends BadRequestException {
    public OriginatorEqualsRecipientException() {
        super("The originator cannot be the same as the recipient.");
    }
}
