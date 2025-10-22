package com.nimble.gateway_payment.shared.services.authorizationService.dtos;

import com.nimble.gateway_payment.shared.services.authorizationService.TypeTransaction;

import java.math.BigDecimal;

public record AuthorizationInputDto(TypeTransaction typeTransaction, String cpf, BigDecimal amount, int numberCard, String exp, int cvv) {
}
