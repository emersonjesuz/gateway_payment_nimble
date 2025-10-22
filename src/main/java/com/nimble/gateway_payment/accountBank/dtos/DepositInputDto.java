package com.nimble.gateway_payment.accountBank.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositInputDto {
    @NotNull(message = "The amount field cannot be empty")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    @DecimalMax(value = "10000.00", inclusive = true, message = "Amount must not exceed R$ 10,000")
    private BigDecimal amount;
}
