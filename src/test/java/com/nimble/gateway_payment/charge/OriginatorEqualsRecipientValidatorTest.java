package com.nimble.gateway_payment.charge;

import com.nimble.gateway_payment.charges.OriginatorEqualsRecipientValidator;
import com.nimble.gateway_payment.charges.exception.OriginatorEqualsRecipientException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OriginatorEqualsRecipientValidatorTest {

    @Test
    public void shouldReturnErrorIfOriginatorCpfEqualRecipientCpf() {
        var originatorCpf = "12345678901";
        var recipientCpf = "12345678901";
        OriginatorEqualsRecipientException exception = assertThrows(OriginatorEqualsRecipientException.class,
                () -> OriginatorEqualsRecipientValidator.validate(originatorCpf, recipientCpf));
        assertEquals("The originator cannot be the same as the recipient.", exception.getMessage());
    }

    @Test
    public void shouldNotReturnErrorIfOriginatorCpfNotEqualRecipientCpf() {
        var originatorCpf = "12345678901";
        var recipientCpf = "12345678902";
        assertDoesNotThrow(
                () -> OriginatorEqualsRecipientValidator.validate(originatorCpf, recipientCpf));

    }
}
