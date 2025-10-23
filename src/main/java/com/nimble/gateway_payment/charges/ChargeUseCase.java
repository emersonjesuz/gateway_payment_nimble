package com.nimble.gateway_payment.charges;

import com.nimble.gateway_payment.accountBank.AccountBankRepository;
import com.nimble.gateway_payment.charges.dtos.ChargeCreateInputDto;
import com.nimble.gateway_payment.charges.dtos.ChargeOutputDto;
import com.nimble.gateway_payment.charges.enums.Status;
import com.nimble.gateway_payment.charges.enums.TypeCharge;
import com.nimble.gateway_payment.user.CpfValidator;
import com.nimble.gateway_payment.user.UserEntity;
import com.nimble.gateway_payment.user.UserRepository;
import com.nimble.gateway_payment.user.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargeUseCase {
    private final UserRepository userRepository;
    private final ChargeRepository chargeRepository;
    private final AccountBankRepository accountBankRepository;

    public ChargeUseCase(UserRepository userRepository, ChargeRepository chargeRepository, AccountBankRepository accountBankRepository) {
        this.chargeRepository = chargeRepository;
        this.userRepository = userRepository;
        this.accountBankRepository = accountBankRepository;
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
}
