package com.nimble.gateway_payment.charge;

import com.nimble.gateway_payment.charges.ChargeEntity;
import com.nimble.gateway_payment.charges.ChargeRepository;
import com.nimble.gateway_payment.charges.ChargeUseCase;
import com.nimble.gateway_payment.charges.Status;
import com.nimble.gateway_payment.user.UserEntity;
import com.nimble.gateway_payment.user.UserRepository;
import com.nimble.gateway_payment.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    @Test
    public void shouldReturnListEmptyIfNotExistCharges() {
        UserEntity user = UserEntity.builder().build();
        when(this.userRepository.findById(any())).thenReturn(Optional.of(user));
        List<ChargeEntity> listMock = new ArrayList<>();
        when(this.chargeRepository.findAllByOriginatorUser(any(), any())).thenReturn(listMock);
        var result = this.chargeUseCase.findAllCreateCharge(Status.PENDING, originatorId);
        assertTrue(result.isEmpty());
    }
}
