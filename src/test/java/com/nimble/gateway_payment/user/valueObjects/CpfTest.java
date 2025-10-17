package com.nimble.gateway_payment.user.valueObjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class CpfTest {

    @Test
    public void shouldReturnAnErrorIfCpfIsNull (){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Cpf(null));
        assertEquals("The CPF cannot be null.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfCPFHasInvalidCharacter (){
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Cpf("111.111.111-30"));
        assertEquals("The CPF must contain only numbers.", exception.getMessage());
    }
}