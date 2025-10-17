package com.nimble.gateway_payment.auth.useCases;

import com.nimble.gateway_payment.auth.dtos.RegisterInputDto;
import com.nimble.gateway_payment.user.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserUseCasesTest {
    @InjectMocks
    private AuthUseCase authUseCase;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAnErrorIfCpfIsNull() {
        RegisterInputDto dto = RegisterInputDto.builder().cpf(null).build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.authUseCase.register(dto);
        });
        assertEquals("The CPF cannot be null.", exception.getMessage());
    }

}
