package com.nimble.gateway_payment.charges;

import com.nimble.gateway_payment.charges.dtos.ChargeCreateInputDto;
import com.nimble.gateway_payment.charges.dtos.ChargeOutputDto;
import com.nimble.gateway_payment.user.CpfValidator;
import com.nimble.gateway_payment.user.UserEntity;
import com.nimble.gateway_payment.user.UserRepository;
import com.nimble.gateway_payment.user.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChargeUseCase {
    private final UserRepository userRepository;
    private final ChargeRepository chargeRepository;

    public ChargeUseCase(UserRepository userRepository, ChargeRepository chargeRepository) {
        this.chargeRepository = chargeRepository;
        this.userRepository = userRepository;
    }

    public void create(ChargeCreateInputDto dto, UUID originatorId) {
        CpfValidator recipientCpf = new CpfValidator(dto.getRecipientCpf());
        UserEntity recipientUser = this.userRepository.findByCpf(recipientCpf.getValue()).orElseThrow(UserNotFoundException::new);
        UserEntity originatorUser = this.userRepository.findById(originatorId).orElseThrow(UserNotFoundException::new);
        OriginatorEqualsRecipientValidator.validate(originatorUser.getCpf(), recipientUser.getCpf());
        ChargeEntity charge = new ChargeEntity();
        charge.setOriginatorUser(originatorUser);
        charge.setRecipientUser(recipientUser);
        charge.setAmount(dto.getAmount());
        charge.setDescription(dto.getDescription() != null ? dto.getDescription() : "");
        this.chargeRepository.save(charge);
    }

    public List<ChargeOutputDto> findAllCreateCharge(Status status, UUID originatorId) {
        UserEntity user = this.userRepository.findById(originatorId).orElseThrow(UserNotFoundException::new);
        List<ChargeEntity> charges = this.chargeRepository.findAllByOriginatorUser(user, status);
        return charges.stream().map(ChargeOutputDto::new).toList();
    }
}
