package com.nimble.gateway_payment.charges;

import com.nimble.gateway_payment.accountBank.AccountBankEntity;
import com.nimble.gateway_payment.accountBank.AccountBankRepository;
import com.nimble.gateway_payment.accountBank.TypePayment;
import com.nimble.gateway_payment.bankStatement.BankStatementEntity;
import com.nimble.gateway_payment.bankStatement.BankStatementRepository;
import com.nimble.gateway_payment.charges.dtos.ChargeCreateInputDto;
import com.nimble.gateway_payment.charges.dtos.ChargeOutputDto;
import com.nimble.gateway_payment.charges.enums.Status;
import com.nimble.gateway_payment.charges.enums.TypeCharge;
import com.nimble.gateway_payment.charges.exception.ChargeNotFoundException;
import com.nimble.gateway_payment.shared.exceptions.InternalServerException;
import com.nimble.gateway_payment.shared.services.authorizationService.AuthorizationService;
import com.nimble.gateway_payment.shared.services.authorizationService.TypeTransaction;
import com.nimble.gateway_payment.shared.services.authorizationService.dtos.AuthorizationInputDto;
import com.nimble.gateway_payment.user.CpfValidator;
import com.nimble.gateway_payment.user.UserEntity;
import com.nimble.gateway_payment.user.UserRepository;
import com.nimble.gateway_payment.user.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ChargeUseCase {
    private final UserRepository userRepository;
    private final ChargeRepository chargeRepository;
    private final AccountBankRepository accountBankRepository;
    private final BankStatementRepository bankStatementRepository;
    private final AuthorizationService authorizationService;

    public ChargeUseCase(UserRepository userRepository, ChargeRepository chargeRepository, AccountBankRepository accountBankRepository,
                         BankStatementRepository bankStatementRepository, AuthorizationService authorizationService) {
        this.chargeRepository = chargeRepository;
        this.userRepository = userRepository;
        this.accountBankRepository = accountBankRepository;
        this.bankStatementRepository = bankStatementRepository;
        this.authorizationService = authorizationService;
    }

    public void create(ChargeCreateInputDto dto, UserEntity originatorUser) {
        CpfValidator recipientCpf = new CpfValidator(dto.getRecipientCpf());
        UserEntity recipientUser = this.userRepository.findByCpf(recipientCpf.getValue()).orElseThrow(UserNotFoundException::new);
        OriginatorEqualsRecipientValidator.validate(originatorUser.getCpf(), recipientUser.getCpf());
        ChargeEntity charge = new ChargeEntity();
        charge.setOriginatorUser(originatorUser);
        charge.setRecipientUser(recipientUser);
        charge.setAmount(dto.getAmount());
        charge.setDescription(dto.getDescription() != null ? dto.getDescription() : "");
        this.chargeRepository.save(charge);
    }

    public List<ChargeOutputDto> findAllCharge(Status status, UserEntity user, TypeCharge typeCharge) {
        List<ChargeEntity> charges;
        if (typeCharge == TypeCharge.ORIGINATOR) {
            charges = this.chargeRepository.findAllByOriginatorUser(user, status);
        } else {
            charges = this.chargeRepository.findAllByRecipientUser(user, status);
        }
        return charges.stream().map(ChargeOutputDto::new).toList();
    }

    @Transactional
    public void canceled(UUID id, UserEntity user) {
        ChargeEntity charge =
                this.chargeRepository.findByIdAndStatusNotAndOriginatorUser(id, Status.CANCELED, user).orElseThrow(ChargeNotFoundException::new);
        if (this.cancelIdPending(charge)) return;
        BankStatementEntity bankStatement = this.bankStatementRepository.findByCharge(charge).orElseThrow(InternalServerException::new);
        AccountBankEntity returnAcc = this.accountBankRepository.findByUsers(user).orElseThrow(InternalServerException::new);
        AccountBankEntity payerAcc = this.accountBankRepository.findByUsers(charge.getRecipientUser()).orElseThrow(InternalServerException::new);
        this.verifyAuthorizedToPaymentCardCredit(bankStatement, user, charge);
        returnAcc.addAmount(charge.getAmount());
        payerAcc.subtractAmount(charge.getAmount());
        charge.setStatus(Status.CANCELED);
        this.chargeRepository.save(charge);
        this.accountBankRepository.save(returnAcc);
        this.accountBankRepository.save(payerAcc);
    }

    private boolean cancelIdPending(ChargeEntity charge) {
        if (charge.getStatus() == Status.PENDING) {
            charge.setStatus(Status.CANCELED);
            this.chargeRepository.save(charge);
            return true;
        }
        return false;
    }

    private void verifyAuthorizedToPaymentCardCredit(BankStatementEntity bankStatement, UserEntity user, ChargeEntity charge) {
        if (bankStatement.getTypePayment() == TypePayment.CARD_CREDIT) {
            AuthorizationInputDto authorizationDto =
                    new AuthorizationInputDto(TypeTransaction.CARD_CREDIT, user.getCpf(), charge.getAmount(), null, null, null);
            this.authorizationService.authorize(authorizationDto);
        }
    }
}
