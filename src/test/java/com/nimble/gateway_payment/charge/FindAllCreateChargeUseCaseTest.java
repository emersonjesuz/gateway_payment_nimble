package com.nimble.gateway_payment.charge;

import com.nimble.gateway_payment.charges.ChargeRepository;
import com.nimble.gateway_payment.charges.ChargeUseCase;
import com.nimble.gateway_payment.charges.Status;
import com.nimble.gateway_payment.user.UserRepository;
import com.nimble.gateway_payment.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class FindAllCreateChargeUseCaseTest {
    private final UUID originatorId = UUID.randomUUID();

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChargeRepository chargeRepository;

    @InjectMocks
    private ChargeUseCase chargeUseCase;

    @Test
    public void shouldReturnAnErrorIfNotExistsUserWithOriginatorId() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            this.chargeUseCase.findAllCreateCharge(Status.PENDING, originatorId);
        });
        assertEquals("User not found.", exception.getMessage());
    }
}
