package com.nimble.gateway_payment.shared.services.authorizationService;

import com.nimble.gateway_payment.shared.exceptions.PaymentRequiredException;
import com.nimble.gateway_payment.shared.services.authorizationService.dtos.AuthorizationInputDto;
import com.nimble.gateway_payment.shared.services.authorizationService.dtos.AuthorizationResponseDto;
import com.nimble.gateway_payment.shared.services.authorizationService.exception.CardDeclinedException;
import com.nimble.gateway_payment.shared.services.authorizationService.exception.DepositAuthorizationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class AuthorizationService {
    private final RestTemplate restTemplate;

    public AuthorizationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean authorize(AuthorizationInputDto dto) {
        try {
            String url = String.format("https://zsy6tx7aql.execute-api.sa-east-1.amazonaws" +
                    ".com/authorizer?value=%s&cpf=%s&numberCard=%s&exp=%s&cvv=%s", dto.amount(), dto.cpf(), dto.numberCard(), dto.exp(), dto.cvv());
            AuthorizationResponseDto response = restTemplate.getForObject(url, AuthorizationResponseDto.class);
            if (Objects.equals(response.status(), "fail")) {
                if (dto.typeTransaction() == TypeTransaction.DEPOSIT) {
                    throw new DepositAuthorizationFailedException();
                }
                throw new CardDeclinedException();
            }
            return response.data().authorized();
        } catch (PaymentRequiredException e) {
            throw new PaymentRequiredException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
