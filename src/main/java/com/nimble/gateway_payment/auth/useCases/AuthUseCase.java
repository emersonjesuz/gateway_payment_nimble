package com.nimble.gateway_payment.auth.useCases;

import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import com.nimble.gateway_payment.user.entities.UserEntity;
import com.nimble.gateway_payment.user.repositories.UserRepository;
import com.nimble.gateway_payment.user.valueObjects.Cpf;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUseCase {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterInputDto dto) {
        Cpf cpf = new Cpf(dto.getCpf());
        if (this.userRepository.findByEmailOrCpf(dto.getEmail(), cpf.getValue()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        var passwordHash = this.passwordEncoder.encode(dto.getPassword());
        UserEntity user = UserEntity.builder()
                .email(dto.getEmail())
                .cpf(cpf.getValue())
                .name(dto.getName())
                .password(passwordHash)
                .build();
        this.userRepository.save(user);
    }
}
