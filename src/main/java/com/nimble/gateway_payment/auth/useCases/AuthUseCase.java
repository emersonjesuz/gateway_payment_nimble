package com.nimble.gateway_payment.auth.useCases;

import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import com.nimble.gateway_payment.user.repositories.UserRepository;
import com.nimble.gateway_payment.user.valueObjects.Cpf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUseCase {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(RegisterInputDto dto) {
        Cpf cpf = new Cpf(dto.getCpf());
        if (this.userRepository.findByEmailOrCPF(dto.getEmail(), cpf.getValue()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
    }
}
