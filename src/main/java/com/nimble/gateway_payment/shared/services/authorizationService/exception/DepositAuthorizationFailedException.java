package com.nimble.gateway_payment.shared.services.authorizationService.exception;

import com.nimble.gateway_payment.shared.exceptions.PaymentRequiredException;

public class DepositAuthorizationFailedException extends PaymentRequiredException {
    public DepositAuthorizationFailedException() {
        super("Deposit authorization failed. Please verify your payment information.");
    }
}
