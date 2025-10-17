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
}