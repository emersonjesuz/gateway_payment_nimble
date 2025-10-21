package com.nimble.gateway_payment.charge;

import com.nimble.gateway_payment.charges.ChargeEntity;
import com.nimble.gateway_payment.charges.ChargeRepository;
import com.nimble.gateway_payment.charges.ChargeUseCase;
import com.nimble.gateway_payment.charges.dtos.ChargeCreateInputDto;
import com.nimble.gateway_payment.charges.exception.OriginatorEqualsRecipientException;
import com.nimble.gateway_payment.user.UserEntity;
import com.nimble.gateway_payment.user.UserRepository;
import com.nimble.gateway_payment.user.exceptions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChargeCreateUseCasesTest {

    private final UUID originatorId = UUID.randomUUID();

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChargeRepository chargeRepository;

    @InjectMocks
    private ChargeUseCase chargeUseCase;

    @Test
    public void shouldReturnAnErrorIfRecipientCpfIsNull() {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf(null).build();
        CpfCannotBeNullException exception = assertThrows(CpfCannotBeNullException.class, () -> {
            this.chargeUseCase.create(dto, this.originatorId);
        });
        assertEquals("The CPF cannot be null.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfRecipientCpfHasInvalidCharacter() {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("111.222.333.44").build();
        CpfMustContainOnlyNumbersException exception = assertThrows(CpfMustContainOnlyNumbersException.class, () -> {
            this.chargeUseCase.create(dto, originatorId);
        });
        assertEquals("The CPF must contain only numbers.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfRecipientCpfNot11NumericDigits() {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("111222333445").build();
        CpfMustContainExactly11NumbersException exception = assertThrows(CpfMustContainExactly11NumbersException.class, () -> {
            this.chargeUseCase.create(dto, originatorId);
        });
        assertEquals("The CPF must contain exactly 11 numeric digits.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfRecipientCpfInvalid() {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("22222222222").build();
        CpfInvalidException exception = assertThrows(CpfInvalidException.class, () -> {
            this.chargeUseCase.create(dto, originatorId);
        });
        assertEquals("CPF inválid.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfNotExistsUserWithRecipientCpf() {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("64717564294").build();
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            this.chargeUseCase.create(dto, originatorId);
        });
        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    public void shouldReturnAnErrorIfNotExistsUserWithOriginatorId() {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("64717564294").build();
        UserEntity user = UserEntity.builder().build();
        when(this.userRepository.findByCpf(any())).thenReturn(Optional.of(user));
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            this.chargeUseCase.create(dto, originatorId);
        });
        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    public void shouldReturnErrorIfOriginatorCpfEqualRecipientCpf() {
        var originatorCpf = "12345678901";
        var recipientCpf = "12345678901";
        UserEntity originatorUser = UserEntity.builder().cpf(originatorCpf).build();
        when(this.userRepository.findByCpf(any())).thenReturn(Optional.of(originatorUser));
        UserEntity recipientUser = UserEntity.builder().cpf(recipientCpf).build();
        when(this.userRepository.findById(any())).thenReturn(Optional.of(recipientUser));
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("64717564294").build();
        OriginatorEqualsRecipientException exception = assertThrows(OriginatorEqualsRecipientException.class,
                () -> this.chargeUseCase.create(dto, originatorId));
        assertEquals("The originator cannot be the same as the recipient.", exception.getMessage());
    }

    @Test
    public void shouldNotReturnErrorIfEverythingIsCorrect() {
        ChargeCreateInputDto dto = ChargeCreateInputDto.builder().recipientCpf("64717564294").amount(BigDecimal.valueOf(200)).build();
        var originatorCpf = "12345678901";
        var recipientCpf = "12345678902";
        UserEntity originatorUser = UserEntity.builder().cpf(originatorCpf).build();
        when(this.userRepository.findByCpf(any())).thenReturn(Optional.of(originatorUser));
        UserEntity recipientUser = UserEntity.builder().cpf(recipientCpf).build();
        when(this.userRepository.findById(any())).thenReturn(Optional.of(recipientUser));
        ChargeEntity charge = ChargeEntity.builder().build();
        when(this.chargeRepository.save(any())).thenReturn(charge);
        assertDoesNotThrow(() -> {
            this.chargeUseCase.create(dto, originatorId);
        });
    }
}
