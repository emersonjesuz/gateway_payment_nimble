package com.nimble.gateway_payment.user.exceptions;

import com.nimble.gateway_payment.shared.exceptions.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("User not found.");
    }
}
