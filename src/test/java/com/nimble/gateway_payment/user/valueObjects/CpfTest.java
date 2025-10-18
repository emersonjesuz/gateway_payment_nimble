package com.nimble.gateway_payment.user.valueObjects;

import com.nimble.gateway_payment.user.Cpf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CpfTest {

    @Test
    public void shouldReturnAnErrorIfCpfIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Cpf(null));
        assertEquals("The CPF cannot be null.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfCPFHasInvalidCharacter() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Cpf("111.111.111-30"));
        assertEquals("The CPF must contain only numbers.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfCPFNot11NumericDigits() {
        var cpfWith9Digits = "123456789";
        var cpfWith12Digits = "123456789012";

        IllegalArgumentException firstException = assertThrows(IllegalArgumentException.class,
                () -> new Cpf(cpfWith9Digits));

        IllegalArgumentException secondException = assertThrows(IllegalArgumentException.class,
                () -> new Cpf(cpfWith12Digits));

        assertEquals("The CPF must contain exactly 11 numeric digits.", firstException.getMessage());
        assertEquals("The CPF must contain exactly 11 numeric digits.", secondException.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfCPFInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Cpf("11111111111"));
        assertEquals("CPF inválid.", exception.getMessage());
    }

    @Test
    public void shouldReturnCPFValid() {
        Cpf cpf1 = new Cpf("64717564294");
        Cpf cpf2 = new Cpf("38485789300");
        Cpf cpf3 = new Cpf("11501002902");

        assertEquals("64717564294", cpf1.getValue());
        assertEquals("38485789300", cpf2.getValue());
        assertEquals("11501002902", cpf3.getValue());
    }
}