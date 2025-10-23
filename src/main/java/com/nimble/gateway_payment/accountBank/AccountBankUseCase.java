package com.nimble.gateway_payment.accountBank;

import com.nimble.gateway_payment.accountBank.dtos.DepositInputDto;
import com.nimble.gateway_payment.shared.exceptions.InternalServerException;
import com.nimble.gateway_payment.shared.services.authorizationService.AuthorizationService;
import com.nimble.gateway_payment.shared.services.authorizationService.TypeTransaction;
import com.nimble.gateway_payment.shared.services.authorizationService.dtos.AuthorizationInputDto;
import com.nimble.gateway_payment.user.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountBankUseCase {
    private final AccountBankRepository accountBankRepository;
    private final AuthorizationService authorizationService;

    public AccountBankUseCase(AccountBankRepository accountBankRepository, AuthorizationService authorizationService) {
        this.accountBankRepository = accountBankRepository;
        this.authorizationService = authorizationService;
    }

    public void deposit(DepositInputDto dto, UserEntity user) {
        AuthorizationInputDto authorizationDto =
                new AuthorizationInputDto(TypeTransaction.DEPOSIT, user.getCpf(), dto.getAmount(), null, null, null);
        this.authorizationService.authorize(authorizationDto);
        this.accountBankRepository.findByCpf(user.getCpf()).orElseThrow(InternalServerException::new);

    }
}
