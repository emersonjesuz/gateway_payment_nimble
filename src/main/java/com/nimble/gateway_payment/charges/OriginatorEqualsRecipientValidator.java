package com.nimble.gateway_payment.charges;

import com.nimble.gateway_payment.charges.exception.OriginatorEqualsRecipientException;

public class OriginatorEqualsRecipientValidator {
    static public void validate(String originatorCpf, String recipientCpf) {
        if (originatorCpf.equals(recipientCpf)) {
            throw new OriginatorEqualsRecipientException();
        }
    }
}
