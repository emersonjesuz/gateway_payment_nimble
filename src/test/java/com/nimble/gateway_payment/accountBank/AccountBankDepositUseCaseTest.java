package com.nimble.gateway_payment.accountBank;

import com.nimble.gateway_payment.accountBank.dtos.DepositInputDto;
import com.nimble.gateway_payment.shared.services.authorizationService.AuthorizationService;
import com.nimble.gateway_payment.shared.services.authorizationService.exception.DepositAuthorizationFailedException;
import com.nimble.gateway_payment.user.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountBankDepositUseCaseTest {

    @Mock
    private AccountBankRepository accountBankRepository;
    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private AccountBankUseCase accountBackUseCase;

    @Test
    public void shouldReturnErrorIfDepositAuthorizationFail() {
        DepositInputDto dto = new DepositInputDto(BigDecimal.TEN);
        UserEntity user = UserEntity.builder().build();
        when(this.authorizationService.authorize(any())).thenThrow(new DepositAuthorizationFailedException());
        DepositAuthorizationFailedException exception = assertThrows(DepositAuthorizationFailedException.class, () -> {
            this.accountBackUseCase.deposit(dto, user);
        });
        assertEquals("Deposit authorization failed. Please verify your payment information.", exception.getMessage());
    }
}
