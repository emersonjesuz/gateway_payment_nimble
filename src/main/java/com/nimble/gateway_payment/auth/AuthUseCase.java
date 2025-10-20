package com.nimble.gateway_payment.auth;

import com.nimble.gateway_payment.auth.dtos.LoginInputDto;
import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import com.nimble.gateway_payment.auth.dtos.exception.IdentifierOrPasswordIncorrectException;
import com.nimble.gateway_payment.shared.security.TokenService;
import com.nimble.gateway_payment.user.CpfValidator;
import com.nimble.gateway_payment.user.UserEntity;
import com.nimble.gateway_payment.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUseCase {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
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

    public String login(LoginInputDto dto) {
        IdentifierValidator identifier = new IdentifierValidator(dto.getIdentifier());
        UserEntity user = this.userRepository.findByEmailOrCpf(identifier.getEmail(), identifier.getCpf())
                .orElseThrow(IdentifierOrPasswordIncorrectException::new);
        if (!this.passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IdentifierOrPasswordIncorrectException();
        }
        return this.tokenService.generateToken(user);
    }
}
