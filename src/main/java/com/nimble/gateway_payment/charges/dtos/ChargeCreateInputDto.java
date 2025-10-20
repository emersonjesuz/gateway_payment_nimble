package com.nimble.gateway_payment.charges.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChargeCreateInputDto {
    @NotBlank(message = "The originatorCpf field cannot be empty")
    @CPF(message = "The originatorCpf field cannot be invalid")
    @JsonIgnore
    private String originatorCpf;

    @NotBlank(message = "The recipientCpf field cannot be empty")
    @CPF(message = "The recipientCpf field cannot be invalid")
    private String recipientCpf;

    @NotBlank(message = "The amount field cannot be empty")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    @DecimalMax(value = "10000.00", inclusive = true, message = "Amount must not exceed R$ 10,000")
    private BigDecimal amount;

    private String description;
}
