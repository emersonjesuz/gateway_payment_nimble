package com.nimble.gateway_payment.accountBank;

import com.nimble.gateway_payment.accountBank.dtos.PaymentInputDto;
import com.nimble.gateway_payment.accountBank.exceptions.BalanceInsufficientException;
import com.nimble.gateway_payment.charges.ChargeEntity;
import com.nimble.gateway_payment.charges.ChargeRepository;
import com.nimble.gateway_payment.charges.enums.Status;
import com.nimble.gateway_payment.charges.exception.ChargeNotFoundException;
import com.nimble.gateway_payment.shared.exceptions.InternalServerException;
import com.nimble.gateway_payment.shared.services.authorizationService.AuthorizationService;
import com.nimble.gateway_payment.shared.services.authorizationService.dtos.AuthorizationInputDto;
import com.nimble.gateway_payment.shared.services.authorizationService.exception.DepositAuthorizationFailedException;
import com.nimble.gateway_payment.user.UserEntity;
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
public class AccountBankPaymentUseCaseTest {

    @Mock
    private AccountBankRepository accountBankRepository;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private ChargeRepository chargeRepository;

    @InjectMocks
    private AccountBankUseCase accountBankUseCase;

    @Test
    public void shouldReturnErrorIfChargeNotFound() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setChargeId(UUID.randomUUID());
        dto.setTypePayment(TypePayment.BALANCE);

        UserEntity user = UserEntity.builder().build();

        when(chargeRepository.findByIdAndStatusAndRecipientUser(any(), any(), any()))
                .thenReturn(Optional.empty());

        ChargeNotFoundException exception = assertThrows(ChargeNotFoundException.class, () -> {
            accountBankUseCase.payment(dto, user);
        });

        assertEquals("Charge not found.", exception.getMessage());
    }

    @Test
    public void shouldReturnErrorIfCreditorAccountNotFound() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setChargeId(UUID.randomUUID());
        dto.setTypePayment(TypePayment.BALANCE);

        UserEntity originator = UserEntity.builder().cpf("12345678900").build();
        UserEntity recipient = UserEntity.builder().cpf("09876543211").build();

        ChargeEntity charge = ChargeEntity.builder()
                .id(dto.getChargeId())
                .status(Status.PENDING)
                .amount(BigDecimal.TEN)
                .originatorUser(originator)
                .recipientUser(recipient)
                .build();

        when(chargeRepository.findByIdAndStatusAndRecipientUser(any(), any(), any()))
                .thenReturn(Optional.of(charge));
        when(accountBankRepository.findByUsers(originator)).thenReturn(Optional.empty());

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            accountBankUseCase.payment(dto, recipient);
        });

        assertEquals("Server error: account not found", exception.getMessage());
    }

    @Test
    public void shouldReturnErrorIfDebtorAccountNotFound() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setChargeId(UUID.randomUUID());
        dto.setTypePayment(TypePayment.BALANCE);

        UserEntity originator = UserEntity.builder().cpf("12345678900").build();
        UserEntity recipient = UserEntity.builder().cpf("09876543211").build();

        ChargeEntity charge = ChargeEntity.builder()
                .id(dto.getChargeId())
                .status(Status.PENDING)
                .amount(BigDecimal.TEN)
                .originatorUser(originator)
                .recipientUser(recipient)
                .build();

        AccountBankEntity creditorAcc = AccountBankEntity.builder().amount(BigDecimal.valueOf(100)).build();

        when(chargeRepository.findByIdAndStatusAndRecipientUser(any(), any(), any()))
                .thenReturn(Optional.of(charge));
        when(accountBankRepository.findByUsers(originator)).thenReturn(Optional.of(creditorAcc));
        when(accountBankRepository.findByUsers(recipient)).thenReturn(Optional.empty());

        InternalServerException exception = assertThrows(InternalServerException.class, () -> {
            accountBankUseCase.payment(dto, recipient);
        });

        assertEquals("Server error: account not found", exception.getMessage());
    }

    @Test
    public void shouldReturnErrorIfBalanceInsufficient() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setChargeId(UUID.randomUUID());
        dto.setTypePayment(TypePayment.BALANCE);

        UserEntity originator = UserEntity.builder().cpf("12345678900").build();
        UserEntity recipient = UserEntity.builder().cpf("09876543211").build();

        ChargeEntity charge = ChargeEntity.builder()
                .id(dto.getChargeId())
                .status(Status.PENDING)
                .amount(BigDecimal.valueOf(100))
                .originatorUser(originator)
                .recipientUser(recipient)
                .build();

        AccountBankEntity creditorAcc = AccountBankEntity.builder().amount(BigDecimal.valueOf(1000)).build();
        AccountBankEntity debtorAcc = AccountBankEntity.builder().amount(BigDecimal.valueOf(50)).build();

        when(chargeRepository.findByIdAndStatusAndRecipientUser(any(), any(), any()))
                .thenReturn(Optional.of(charge));
        when(accountBankRepository.findByUsers(originator)).thenReturn(Optional.of(creditorAcc));
        when(accountBankRepository.findByUsers(recipient)).thenReturn(Optional.of(debtorAcc));

        BalanceInsufficientException exception = assertThrows(BalanceInsufficientException.class, () -> {
            accountBankUseCase.payment(dto, recipient);
        });

        assertEquals("Balance insufficient.", exception.getMessage());
    }

    @Test
    public void shouldReturnErrorIfCardAuthorizationFail() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setChargeId(UUID.randomUUID());
        dto.setTypePayment(TypePayment.CARD_CREDIT);
        dto.setCardNumber("4111111111111111");
        dto.setExp("12/29");
        dto.setCvv("123");

        UserEntity originator = UserEntity.builder().cpf("12345678900").build();
        UserEntity recipient = UserEntity.builder().cpf("09876543211").build();

        ChargeEntity charge = ChargeEntity.builder()
                .id(dto.getChargeId())
                .status(Status.PENDING)
                .amount(BigDecimal.TEN)
                .originatorUser(originator)
                .recipientUser(recipient)
                .build();

        AccountBankEntity creditorAcc = AccountBankEntity.builder().amount(BigDecimal.ZERO).build();
        AccountBankEntity debtorAcc = AccountBankEntity.builder().amount(BigDecimal.ZERO).build();

        when(chargeRepository.findByIdAndStatusAndRecipientUser(any(), any(), any()))
                .thenReturn(Optional.of(charge));
        when(accountBankRepository.findByUsers(originator)).thenReturn(Optional.of(creditorAcc));
        when(accountBankRepository.findByUsers(recipient)).thenReturn(Optional.of(debtorAcc));
        when(authorizationService.authorize(any(AuthorizationInputDto.class)))
                .thenThrow(new DepositAuthorizationFailedException());

        DepositAuthorizationFailedException exception = assertThrows(DepositAuthorizationFailedException.class, () -> {
            accountBankUseCase.payment(dto, recipient);
        });

        assertEquals("Deposit authorization failed. Please verify your payment information.", exception.getMessage());
    }

    @Test
    public void shouldNotReturnErrorIfPaymentByBalanceSuccess() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setChargeId(UUID.randomUUID());
        dto.setTypePayment(TypePayment.BALANCE);

        UserEntity originator = UserEntity.builder().cpf("12345678900").build();
        UserEntity recipient = UserEntity.builder().cpf("09876543211").build();

        ChargeEntity charge = ChargeEntity.builder()
                .id(dto.getChargeId())
                .status(Status.PENDING)
                .amount(BigDecimal.valueOf(100))
                .originatorUser(originator)
                .recipientUser(recipient)
                .build();

        AccountBankEntity creditorAcc = AccountBankEntity.builder().amount(BigDecimal.valueOf(500)).build();
        AccountBankEntity debtorAcc = AccountBankEntity.builder().amount(BigDecimal.valueOf(500)).build();

        when(chargeRepository.findByIdAndStatusAndRecipientUser(any(), any(), any()))
                .thenReturn(Optional.of(charge));
        when(accountBankRepository.findByUsers(originator)).thenReturn(Optional.of(creditorAcc));
        when(accountBankRepository.findByUsers(recipient)).thenReturn(Optional.of(debtorAcc));

        assertDoesNotThrow(() -> {
            accountBankUseCase.payment(dto, recipient);
        });
    }

    @Test
    public void shouldNotReturnErrorIfPaymentByCardSuccess() {
        PaymentInputDto dto = new PaymentInputDto();
        dto.setChargeId(UUID.randomUUID());
        dto.setTypePayment(TypePayment.CARD_CREDIT);
        dto.setCardNumber("4111111111111111");
        dto.setExp("12/29");
        dto.setCvv("123");

        UserEntity originator = UserEntity.builder().cpf("12345678900").build();
        UserEntity recipient = UserEntity.builder().cpf("09876543211").build();

        ChargeEntity charge = ChargeEntity.builder()
                .id(dto.getChargeId())
                .status(Status.PENDING)
                .amount(BigDecimal.valueOf(100))
                .originatorUser(originator)
                .recipientUser(recipient)
                .build();

        AccountBankEntity creditorAcc = AccountBankEntity.builder().amount(BigDecimal.valueOf(500)).build();
        AccountBankEntity debtorAcc = AccountBankEntity.builder().amount(BigDecimal.valueOf(100)).build();

        when(chargeRepository.findByIdAndStatusAndRecipientUser(any(), any(), any()))
                .thenReturn(Optional.of(charge));
        when(accountBankRepository.findByUsers(originator)).thenReturn(Optional.of(creditorAcc));
        when(accountBankRepository.findByUsers(recipient)).thenReturn(Optional.of(debtorAcc));
        when(authorizationService.authorize(any())).thenReturn(true);

        assertDoesNotThrow(() -> {
            accountBankUseCase.payment(dto, recipient);
        });
    }
}
