package com.nimble.gateway_payment.accountBank;

import com.nimble.gateway_payment.accountBank.dtos.PaymentInputDto;
import com.nimble.gateway_payment.accountBank.exceptions.InvalidCardDataException;

public class PaymentByCardValidator {
    public static void validate(PaymentInputDto dto) {
        if (dto.getTypePayment() == TypePayment.CARD_CREDIT) {
            if (dto.getCardNumber() == null || dto.getExp() == null || dto.getCvv() == null) {
                throw new InvalidCardDataException();
            }

            if (!dto.getCardNumber().matches("\\d+") || !dto.getExp().matches("^[\\d/]+$") || !dto.getCvv().matches("\\d+")) {
                throw new InvalidCardDataException();
            }
        }
    }
}
