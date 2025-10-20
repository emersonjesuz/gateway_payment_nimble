package com.nimble.gateway_payment.user.exceptions;

import com.nimble.gateway_payment.shared.exceptions.BadRequestException;

public class UserAlreadyExistsException extends BadRequestException {
    public UserAlreadyExistsException() {
        super("User already exists");
    }
}
