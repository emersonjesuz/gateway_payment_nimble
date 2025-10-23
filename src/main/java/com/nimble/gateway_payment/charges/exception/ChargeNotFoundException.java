package com.nimble.gateway_payment.charges.exception;

import com.nimble.gateway_payment.shared.exceptions.NotFoundException;

public class ChargeNotFoundException extends NotFoundException {
    public ChargeNotFoundException() {
        super("Charge not found.");
    }
}
