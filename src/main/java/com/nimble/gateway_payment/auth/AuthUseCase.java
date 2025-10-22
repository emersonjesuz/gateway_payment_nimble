package com.nimble.gateway_payment.auth;

import com.nimble.gateway_payment.accountBank.AccountBankEntity;
import com.nimble.gateway_payment.accountBank.AccountBankRepository;
import com.nimble.gateway_payment.auth.dtos.LoginInputDto;
import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import com.nimble.gateway_payment.auth.exception.IdentifierOrPasswordIncorrectException;
import com.nimble.gateway_payment.shared.security.TokenService;
import com.nimble.gateway_payment.user.CpfValidator;
import com.nimble.gateway_payment.user.UserEntity;
import com.nimble.gateway_payment.user.UserRepository;
import com.nimble.gateway_payment.user.exceptions.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AuthUseCase {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AccountBankRepository accountBankRepository;

    public AuthUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, AccountBankRepository accountBankRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.accountBankRepository = accountBankRepository;
    }

    @Transactional
    public void register(RegisterInputDto dto) {
        CpfValidator cpf = new CpfValidator(dto.getCpf());
        if (this.userRepository.findByEmailOrCpf(dto.getEmail(), cpf.getValue()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        var passwordHash = this.passwordEncoder.encode(dto.getPassword());
        UserEntity user = UserEntity.builder()
                .email(dto.getEmail())
                .cpf(cpf.getValue())
                .name(dto.getName())
                .password(passwordHash)
                .build();
        AccountBankEntity account = AccountBankEntity.builder().amount(BigDecimal.ZERO).users(user).createdAt(LocalDateTime.now()).build();
        this.userRepository.save(user);
        this.accountBankRepository.save(account);
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
