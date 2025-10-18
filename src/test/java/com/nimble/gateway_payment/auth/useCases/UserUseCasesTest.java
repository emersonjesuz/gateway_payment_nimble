package com.nimble.gateway_payment.auth.useCases;

import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import com.nimble.gateway_payment.user.entities.UserEntity;
import com.nimble.gateway_payment.user.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserUseCasesTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthUseCase authUseCase;

    @Test
    public void shouldReturnAnErrorIfCpfIsNull() {
        RegisterInputDto dto = RegisterInputDto.builder().cpf(null).build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.authUseCase.register(dto);
        });
        assertEquals("The CPF cannot be null.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfCPFHasInvalidCharacter() {
        RegisterInputDto dto = RegisterInputDto.builder().cpf("111.222.333.10").build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.authUseCase.register(dto);
        });
        assertEquals("The CPF must contain only numbers.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfCPFNot11NumericDigits() {
        RegisterInputDto dto = RegisterInputDto.builder().cpf("1122233310").build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.authUseCase.register(dto);
        });
        assertEquals("The CPF must contain exactly 11 numeric digits.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfCPFInvalid() {
        RegisterInputDto dto = RegisterInputDto.builder().cpf("22222222222").build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.authUseCase.register(dto);
        });
        assertEquals("CPF inválid.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfExistsUserWithEmailOrCpf() {
        RegisterInputDto dto = RegisterInputDto.builder()
                .email("josi@email.com")
                .cpf("64717564294")
                .build();

        UserEntity user = UserEntity.builder()
                .email("josi@email.com")
                .cpf("64717564294")
                .name("josi")
                .password("josi123")
                .build();

        when(this.userRepository.findByEmailOrCpf(anyString(), anyString()))
                .thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.authUseCase.register(dto);
        });

        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    public void shouldNotReturnAnErrorIfEverythingIsCorrect() {
        RegisterInputDto dto = RegisterInputDto.builder()
                .email("josi@email.com")
                .cpf("64717564294")
                .password("password1234")
                .build();

        UserEntity user = UserEntity.builder()
                .email("josi@email.com")
                .cpf("64717564294")
                .name("josi")
                .password("josi123")
                .build();

        when(this.userRepository.findByEmailOrCpf(anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(this.passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(this.userRepository.save(any(UserEntity.class)))
                .thenReturn(user);

        assertDoesNotThrow(() -> authUseCase.register(dto));
    }
}
