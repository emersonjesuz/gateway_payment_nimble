package com.nimble.gateway_payment.user;

import com.nimble.gateway_payment.user.exceptions.CpfCannotBeNullException;
import com.nimble.gateway_payment.user.exceptions.CpfMustContainOnlyNumbersException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CpfValidationTest {

    @Test
    public void shouldReturnAnErrorIfCpfIsNull() {
        CpfCannotBeNullException exception = assertThrows(CpfCannotBeNullException.class,
                () -> new CpfValidator(null));
        assertEquals("The CPF cannot be null.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfCPFHasInvalidCharacter() {
        CpfMustContainOnlyNumbersException exception = assertThrows(CpfMustContainOnlyNumbersException.class,
                () -> new CpfValidator("111.111.111-30"));
        assertEquals("The CPF must contain only numbers.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfCPFNot11NumericDigits() {
        var cpfWith9Digits = "123456789";
        var cpfWith12Digits = "123456789012";

        IllegalArgumentException firstException = assertThrows(IllegalArgumentException.class,
                () -> new CpfValidator(cpfWith9Digits));

        IllegalArgumentException secondException = assertThrows(IllegalArgumentException.class,
                () -> new CpfValidator(cpfWith12Digits));

        assertEquals("The CPF must contain exactly 11 numeric digits.", firstException.getMessage());
        assertEquals("The CPF must contain exactly 11 numeric digits.", secondException.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfCPFInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new CpfValidator("11111111111"));
        assertEquals("CPF inválid.", exception.getMessage());
    }

    @Test
    public void shouldReturnCPFValid() {
        CpfValidator cpf1 = new CpfValidator("64717564294");
        CpfValidator cpf2 = new CpfValidator("38485789300");
        CpfValidator cpf3 = new CpfValidator("11501002902");

        assertEquals("64717564294", cpf1.getValue());
        assertEquals("38485789300", cpf2.getValue());
        assertEquals("11501002902", cpf3.getValue());
    }
}