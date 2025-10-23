package com.nimble.gateway_payment.accountBank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimble.gateway_payment.accountBank.dtos.PaymentInputDto;
import com.nimble.gateway_payment.charges.ChargeEntity;
import com.nimble.gateway_payment.charges.enums.Status;
import com.nimble.gateway_payment.shared.services.authorizationService.AuthorizationService;
import com.nimble.gateway_payment.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountBankPaymentControllerTest {

    @InjectMocks
    private AccountBankController controller;

    @Mock
    private AccountBankUseCase accountBankUseCase;

    @Mock
    private AuthorizationService authorizationService;

    private MockMvc mvc;
    private ObjectMapper objectMapper;

    private UserEntity originatorUser;
    private UserEntity recipientUser;
    private AccountBankEntity originatorAccount;
    private AccountBankEntity recipientAccount;
    private ChargeEntity charge;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        originatorUser = UserEntity.builder()
                .id(UUID.randomUUID())
                .cpf("11122233344")
                .build();

        recipientUser = UserEntity.builder()
                .id(UUID.randomUUID())
                .cpf("99988877766")
                .build();

        originatorAccount = AccountBankEntity.builder()
                .id(UUID.randomUUID())
                .users(originatorUser)
                .amount(BigDecimal.valueOf(1000))
                .build();

        recipientAccount = AccountBankEntity.builder()
                .id(UUID.randomUUID())
                .users(recipientUser)
                .amount(BigDecimal.valueOf(200))
                .build();

        charge = ChargeEntity.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(150))
                .status(Status.PENDING)
                .originatorUser(originatorUser)
                .recipientUser(recipientUser)
                .build();
    }

    @Test
    void shouldReturn201IfPaymentByBalanceSuccess() throws Exception {
        PaymentInputDto dto = PaymentInputDto.builder()
                .typePayment(TypePayment.BALANCE)
                .chargeId(charge.getId())
                .build();

        mvc.perform(post("/account/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .requestAttr("user", recipientUser))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Payment success."));
    }

    @Test
    void shouldReturn201IfPaymentByCardSuccess() throws Exception {
        PaymentInputDto dto = PaymentInputDto.builder()
                .typePayment(TypePayment.CARD_CREDIT)
                .chargeId(charge.getId())
                .cardNumber("4111111111111111")
                .exp("12/29")
                .cvv("123")
                .build();

        lenient().when(authorizationService.authorize(any())).thenReturn(true);

        mvc.perform(post("/account/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .requestAttr("user", recipientUser))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Payment success."));
    }

    @Test
    void shouldReturn400IfMissingFields() throws Exception {
        PaymentInputDto dto = new PaymentInputDto();

        mvc.perform(post("/account/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .requestAttr("user", recipientUser))
                .andExpect(status().isBadRequest());
    }
}
