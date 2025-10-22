package com.nimble.gateway_payment.shared.services.authorizationService.exception;

import com.nimble.gateway_payment.shared.exceptions.PaymentRequiredException;

public class CardDeclinedException extends PaymentRequiredException {
    public CardDeclinedException() {
        super("Your card has been declined. Please check your details.");
    }
}
