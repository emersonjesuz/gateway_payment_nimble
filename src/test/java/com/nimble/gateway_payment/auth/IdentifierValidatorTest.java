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
        IdentifierValidator identifier = new IdentifierValidator("12345678912");
        assertEquals(null, identifier.getEmail());
    }

    @Test
    public void shouldReturnEmailIfIdentifyIsEmail() {
        IdentifierValidator identifier = new IdentifierValidator("josi@email.com");
        assertEquals("josi@email.com", identifier.getEmail());
    }

    @Test
    public void shouldReturnNullIfIdentifyIsNotCpf() {
        IdentifierValidator identifier = new IdentifierValidator("josi@email.com");
        assertEquals(null, identifier.getCpf());
    }

    @Test
    public void shouldReturnCpfIfIdentifyIsCpf() {
        IdentifierValidator identifier = new IdentifierValidator("12345678912");
        assertEquals("12345678912", identifier.getCpf());
    }

    @Test
    public void shouldReturnErrorIfIdentifierIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new IdentifierValidator("identifier"));
        assertEquals("Identifier invalid.", exception.getMessage());
    }
}
