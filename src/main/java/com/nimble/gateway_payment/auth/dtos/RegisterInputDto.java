package com.nimble.gateway_payment.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterInputDto {
    @NotBlank(message = "The name field cannot be empty")
    private String name;
    @NotBlank(message = "The email field cannot be empty")
    @Email(message = "The email field cannot be invalid")
    private String email;
    @NotBlank(message = "The CPF field cannot be empty")
    @CPF(message = "The CPF field cannot be invalid")
    private String cpf;
    @NotBlank(message = "The name field cannot be empty")
    @Min(value = 6, message = "The Password field must have at least 6 digits")
    private String password;
}
