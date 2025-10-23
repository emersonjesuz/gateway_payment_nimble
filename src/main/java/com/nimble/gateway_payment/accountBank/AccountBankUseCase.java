package com.nimble.gateway_payment.accountBank;

import com.nimble.gateway_payment.accountBank.dtos.DepositInputDto;
import com.nimble.gateway_payment.accountBank.dtos.PaymentInputDto;
import com.nimble.gateway_payment.accountBank.exceptions.BalanceInsufficientException;
import com.nimble.gateway_payment.charges.ChargeEntity;
import com.nimble.gateway_payment.charges.ChargeRepository;
import com.nimble.gateway_payment.charges.enums.Status;
import com.nimble.gateway_payment.charges.exception.ChargeNotFoundException;
import com.nimble.gateway_payment.shared.exceptions.InternalServerException;
import com.nimble.gateway_payment.shared.services.authorizationService.AuthorizationService;
import com.nimble.gateway_payment.shared.services.authorizationService.TypeTransaction;
import com.nimble.gateway_payment.shared.services.authorizationService.dtos.AuthorizationInputDto;
import com.nimble.gateway_payment.user.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountBankUseCase {
    private final AccountBankRepository accountBankRepository;
    private final AuthorizationService authorizationService;
    private final ChargeRepository chargeRepository;

    public AccountBankUseCase(AccountBankRepository accountBankRepository, AuthorizationService authorizationService, ChargeRepository chargeRepository) {
        this.accountBankRepository = accountBankRepository;
        this.authorizationService = authorizationService;
        this.chargeRepository = chargeRepository;
    }

    public void deposit(DepositInputDto dto, UserEntity user) {
        AuthorizationInputDto authorizationDto =
                new AuthorizationInputDto(TypeTransaction.DEPOSIT, user.getCpf(), dto.getAmount(), null, null, null);
        this.authorizationService.authorize(authorizationDto);
        AccountBankEntity account = this.accountBankRepository.findByUsers(user).orElseThrow(InternalServerException::new);
        account.setAmount(account.getAmount().add(dto.getAmount()));
        this.accountBankRepository.save(account);
    }

    @Transactional
    public void payment(PaymentInputDto dto, UserEntity user) {
        PaymentByCardValidator.validate(dto);
        ChargeEntity charge = this.chargeRepository.findByIdAndStatusAndRecipientUser(dto.getChargeId(), Status.PENDING, user).orElseThrow(ChargeNotFoundException::new);

        AccountBankEntity creditorAcc = this.accountBankRepository.findByUsers(charge.getOriginatorUser()).orElseThrow(InternalServerException::new);
        AccountBankEntity debtorAcc = this.accountBankRepository.findByUsers(user).orElseThrow(InternalServerException::new);

        if (dto.getTypePayment() == TypePayment.CARD_CREDIT) {
            AuthorizationInputDto authorizationDto =
                    new AuthorizationInputDto(TypeTransaction.DEPOSIT, user.getCpf(), charge.getAmount(), dto.getCardNumber(), dto.getExp(), dto.getCvv());
            this.authorizationService.authorize(authorizationDto);
            debtorAcc.addAmount(charge.getAmount());
        } else {
            if (debtorAcc.getAmount().compareTo(charge.getAmount()) < 0) {
                throw new BalanceInsufficientException();
            }
            creditorAcc.addAmount(charge.getAmount());
            debtorAcc.subtractAmount(charge.getAmount());
        }

        charge.setStatus(Status.PAID);

        this.chargeRepository.save(charge);
        this.accountBankRepository.save(creditorAcc);
        this.accountBankRepository.save(debtorAcc);
    }
}
