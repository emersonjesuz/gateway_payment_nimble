package com.nimble.gateway_payment.charges;

import com.nimble.gateway_payment.charges.dtos.ChargeCreateInputDto;
import com.nimble.gateway_payment.user.CpfValidator;
import com.nimble.gateway_payment.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ChargeUseCase {
    private final UserRepository userRepository;
    private final ChargeRepository chargeRepository;

    public ChargeUseCase(UserRepository userRepository, ChargeRepository chargeRepository) {
        this.chargeRepository = chargeRepository;
        this.userRepository = userRepository;
    }

    public void create(ChargeCreateInputDto dto) {
        new CpfValidator(dto.getRecipientCpf());
    }
}
