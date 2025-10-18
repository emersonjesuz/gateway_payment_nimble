package com.nimble.gateway_payment.auth;

import com.nimble.gateway_payment.auth.dtos.LoginInputDto;
import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import com.nimble.gateway_payment.user.CpfValidator;
import com.nimble.gateway_payment.user.UserEntity;
import com.nimble.gateway_payment.user.UserRepository;
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
        CpfValidator cpf = new CpfValidator(dto.getCpf());
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

    public void login(LoginInputDto dto) {
        new IdentifierValidator(dto.getIdentifier());
    }
}
