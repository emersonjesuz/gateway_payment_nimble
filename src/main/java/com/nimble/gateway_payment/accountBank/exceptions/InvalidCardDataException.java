package com.nimble.gateway_payment.accountBank.exceptions;

import com.nimble.gateway_payment.shared.exceptions.BadRequestException;

public class InvalidCardDataException extends BadRequestException {
    public InvalidCardDataException() {
        super("Invalid card details: Card details are required");
    }
}
