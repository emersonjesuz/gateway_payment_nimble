package com.nimble.gateway_payment.charge;

import com.nimble.gateway_payment.charges.ChargeRepository;
import com.nimble.gateway_payment.charges.ChargeUseCase;
import com.nimble.gateway_payment.charges.dtos.ChargeCreateInputDto;
import com.nimble.gateway_payment.user.UserRepository;
import com.nimble.gateway_payment.user.exceptions.CpfCannotBeNullException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ChargeCreateUseCasesTest {

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
            this.chargeUseCase.create(dto);
        });
        assertEquals("The CPF cannot be null.", exception.getMessage());
    }

}
