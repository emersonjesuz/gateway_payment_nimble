package com.nimble.gateway_payment.auth;

import com.nimble.gateway_payment.auth.dtos.LoginInputDto;
import com.nimble.gateway_payment.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
