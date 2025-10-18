package com.nimble.gateway_payment.auth;

import com.nimble.gateway_payment.auth.dtos.LoginInputDto;
import com.nimble.gateway_payment.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthLoginUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthUseCase authUseCase;

    @Test
    public void shouldReturnErrorIfIdentifierIsEmpty() {
        LoginInputDto dto = new LoginInputDto("", "password");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.authUseCase.login(dto));
        assertEquals("Identifier is not empty.", exception.getMessage());
    }

    @Test
    public void shouldReturnErrorIfIdentifierIsInvalid() {
        LoginInputDto dto = new LoginInputDto("invalid", "password");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.authUseCase.login(dto));
        assertEquals("Identifier invalid.", exception.getMessage());
    }

    @Test
    public void shouldReturnErrorIfNotFindUserByIdentifierEmail() {
        LoginInputDto dto = new LoginInputDto("josi@email.com", "password");
        when(this.userRepository.findByEmailOrCpf(any(), any())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.authUseCase.login(dto));
        assertEquals("Identifier or password incorrect.", exception.getMessage());
    }

    @Test
    public void shouldReturnErrorIfNotFindUserByIdentifierCpf() {
        LoginInputDto dto = new LoginInputDto("11122233344", "password");
        when(this.userRepository.findByEmailOrCpf(any(), any())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.authUseCase.login(dto));
        assertEquals("Identifier or password incorrect.", exception.getMessage());
    }
}
