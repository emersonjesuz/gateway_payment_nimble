package com.nimble.gateway_payment.accountBank;

import com.nimble.gateway_payment.accountBank.dtos.PaymentInputDto;
import com.nimble.gateway_payment.accountBank.exceptions.InvalidCardDataException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentByCardValidatorTest {

    @Test
    void shouldReturnErrorIfCardNumberNull() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setTypePayment(TypePayment.CARD_CREDIT);
        dto.setExp("12");
        dto.setCvv("123");
        dto.setChargeId(UUID.randomUUID());

        assertThrows(InvalidCardDataException.class, () -> PaymentByCardValidator.validate(dto));
    }

    @Test
    void shouldReturnErrorIfExpIsNull() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setTypePayment(TypePayment.CARD_CREDIT);
        dto.setCardNumber("1234567890123456");
        dto.setCvv("123");
        dto.setChargeId(UUID.randomUUID());

        assertThrows(InvalidCardDataException.class, () -> PaymentByCardValidator.validate(dto));
    }

    @Test
    void shouldReturnErrorIfCvvIsNull() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setTypePayment(TypePayment.CARD_CREDIT);
        dto.setCardNumber("1234567890123456");
        dto.setExp("12");
        dto.setChargeId(UUID.randomUUID());

        assertThrows(InvalidCardDataException.class, () -> PaymentByCardValidator.validate(dto));
    }

    @Test
    void shouldReturnErrorIfCardNumberHasLetters() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setTypePayment(TypePayment.CARD_CREDIT);
        dto.setCardNumber("1234abcd5678");
        dto.setExp("12");
        dto.setCvv("123");
        dto.setChargeId(UUID.randomUUID());

        assertThrows(InvalidCardDataException.class, () -> PaymentByCardValidator.validate(dto));
    }

    @Test
    void shouldReturnErrorIfExpHasLetters() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setTypePayment(TypePayment.CARD_CREDIT);
        dto.setCardNumber("1234567890123456");
        dto.setExp("1a");
        dto.setCvv("123");
        dto.setChargeId(UUID.randomUUID());

        assertThrows(InvalidCardDataException.class, () -> PaymentByCardValidator.validate(dto));
    }

    @Test
    void shouldReturnErrorIfCvvHasLetters() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setTypePayment(TypePayment.CARD_CREDIT);
        dto.setCardNumber("1234567890123456");
        dto.setExp("12");
        dto.setCvv("1b3");
        dto.setChargeId(UUID.randomUUID());

        assertThrows(InvalidCardDataException.class, () -> PaymentByCardValidator.validate(dto));
    }

    @Test
    void shouldNotReturnErrorIfCardIsValid() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setTypePayment(TypePayment.CARD_CREDIT);
        dto.setCardNumber("1234567890123456");
        dto.setExp("12");
        dto.setCvv("123");
        dto.setChargeId(UUID.randomUUID());

        assertDoesNotThrow(() -> PaymentByCardValidator.validate(dto));
    }

}
