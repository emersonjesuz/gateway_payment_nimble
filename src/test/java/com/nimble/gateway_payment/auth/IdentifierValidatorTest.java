package com.nimble.gateway_payment.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IdentifierValidatorTest {

    @Test
    public void shouldReturnErrorIfIdentifierIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new IdentifierValidator(""));
        assertEquals("Identifier is not empty.", exception.getMessage());
    }

    @Test
    public void shouldReturnNullIfIdentifyIsNotEmail() {
        IdentifierValidator identifier = new IdentifierValidator("notEmail");
        assertEquals(null, identifier.getEmail());
    }

    @Test
    public void shouldReturnEmailIfIdentifyIsEmail() {
        IdentifierValidator identifier = new IdentifierValidator("josi@email.com");
        assertEquals("josi@email.com", identifier.getEmail());
    }

    @Test
    public void shouldReturnNullIfIdentifyIsNotCpf() {
        IdentifierValidator identifier = new IdentifierValidator("notCpf");
        assertEquals(null, identifier.getCpf());
    }
}
