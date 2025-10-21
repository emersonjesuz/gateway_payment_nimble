package com.nimble.gateway_payment.charges;

import com.nimble.gateway_payment.charges.dtos.ChargeCreateInputDto;
import com.nimble.gateway_payment.user.CpfValidator;
import com.nimble.gateway_payment.user.UserRepository;
import com.nimble.gateway_payment.user.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

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
        this.userRepository.findByCpf(recipientCpf.getValue()).orElseThrow(UserNotFoundException::new);
        this.userRepository.findById(originatorId).orElseThrow(UserNotFoundException::new);

    }
}
