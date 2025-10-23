package com.nimble.gateway_payment.accountBank.exceptions;

import com.nimble.gateway_payment.shared.exceptions.BadRequestException;

public class BalanceInsufficientException extends BadRequestException {
    public BalanceInsufficientException() {
        super("Balance insufficient.");
    }
}
