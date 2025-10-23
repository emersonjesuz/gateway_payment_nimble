package com.nimble.gateway_payment.charge;

import com.nimble.gateway_payment.ChargeListToMock;
import com.nimble.gateway_payment.charges.ChargeEntity;
import com.nimble.gateway_payment.charges.ChargeRepository;
import com.nimble.gateway_payment.charges.ChargeUseCase;
import com.nimble.gateway_payment.charges.enums.Status;
import com.nimble.gateway_payment.charges.enums.TypeCharge;
import com.nimble.gateway_payment.user.UserEntity;
import com.nimble.gateway_payment.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAllChargeUseCaseTest {
    private final UserEntity user = UserEntity.builder().build();

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChargeRepository chargeRepository;

    @InjectMocks
    private ChargeUseCase chargeUseCase;

    @Test
    public void shouldReturnListEmptyIfNotExistChargesTypeUserOriginator() {
        List<ChargeEntity> listMock = new ArrayList<>();
        when(this.chargeRepository.findAllByOriginatorUser(any(), any())).thenReturn(listMock);
        var result = this.chargeUseCase.findAllCharge(Status.PENDING, this.user, TypeCharge.ORIGINATOR);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnListEmptyIfNotExistChargesTypeUserRecipient() {
        List<ChargeEntity> listMock = new ArrayList<>();
        when(this.chargeRepository.findAllByRecipientUser(any(), any())).thenReturn(listMock);
        var result = this.chargeUseCase.findAllCharge(Status.PENDING, this.user, TypeCharge.RECIPIENT);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnListChargesTypeUserOriginator() {
        UserEntity user1 = UserEntity.builder().build();
        UserEntity user2 = UserEntity.builder().build();
        List<ChargeEntity> listMock = new ArrayList<>(ChargeListToMock.data(user1, user2, user1, user2));
        when(this.chargeRepository.findAllByOriginatorUser(any(), any())).thenReturn(listMock);
        var result = this.chargeUseCase.findAllCharge(Status.PENDING, user, TypeCharge.ORIGINATOR);
        assertEquals(4, result.size());
    }

    @Test
    public void shouldReturnListChargesTypeUserRecipient() {
        UserEntity user1 = UserEntity.builder().id(UUID.randomUUID()).build();
        UserEntity user2 = UserEntity.builder().id(UUID.randomUUID()).build();
        List<ChargeEntity> listMock = new ArrayList<>(ChargeListToMock.data(user1, user2, user1, user2));
        when(this.chargeRepository.findAllByRecipientUser(any(), any())).thenReturn(listMock);
        var result = this.chargeUseCase.findAllCharge(Status.PENDING, user, TypeCharge.RECIPIENT);
        assertEquals(4, result.size());
    }
}
