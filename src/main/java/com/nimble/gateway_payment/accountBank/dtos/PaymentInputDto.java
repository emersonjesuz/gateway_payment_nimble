package com.nimble.gateway_payment.accountBank.dtos;

import com.nimble.gateway_payment.accountBank.TypePayment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInputDto {
    @NotNull(message = "the field typePayment only accepts CARD_CREDIT or BALANCE")
    private TypePayment typePayment;

    @NotNull(message = "The field chargeId is obligatory.")
    private UUID chargeId;
    private String cardNumber;
    private String exp;
    private String cvv;
}
