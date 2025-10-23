package com.nimble.gateway_payment.accountBank;

import com.nimble.gateway_payment.accountBank.dtos.DepositInputDto;
import com.nimble.gateway_payment.accountBank.dtos.DepositOutputDto;
import com.nimble.gateway_payment.user.UserEntity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountBankController {
    private final AccountBankUseCase accountBackUseCase;

    public AccountBankController(AccountBankUseCase accountBackUseCase) {
        this.accountBackUseCase = accountBackUseCase;
    }

    @PostMapping("/deposit")
    public ResponseEntity<DepositOutputDto> deposit(@RequestBody @Valid DepositInputDto body, @AuthenticationPrincipal UserEntity user) {
        this.accountBackUseCase.deposit(body, user);
        return ResponseEntity.status(201).body(new DepositOutputDto("Deposit success."));
    }
}
