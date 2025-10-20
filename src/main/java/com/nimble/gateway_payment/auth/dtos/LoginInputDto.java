package com.nimble.gateway_payment.auth.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginInputDto {
    @NotBlank(message = "The identifier field requires email or CPF.")
    private String identifier;
    @NotBlank(message = "The password field require.")
    private String password;
}
