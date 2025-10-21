package com.nimble.gateway_payment.charge;

import com.nimble.gateway_payment.charges.OriginatorEqualsRecipientValidator;
import com.nimble.gateway_payment.charges.exception.OriginatorEqualsRecipientException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OriginatorEqualsRecipientValidatorTest {

    @Test
    public void shouldReturnErrorIfOriginatorCpfEqualRecipientCpf() {
        var originatorCpf = "12345678901";
        var recipientCpf = "12345678901";
        OriginatorEqualsRecipientException exception = assertThrows(OriginatorEqualsRecipientException.class,
                () -> OriginatorEqualsRecipientValidator.validate(originatorCpf, recipientCpf));
        assertEquals("The originator cannot be the same as the recipient.", exception.getMessage());
    }
}
