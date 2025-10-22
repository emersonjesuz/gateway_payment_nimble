package com.nimble.gateway_payment;

import com.nimble.gateway_payment.charges.ChargeEntity;
import com.nimble.gateway_payment.charges.enums.Status;
import com.nimble.gateway_payment.user.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ChargeListToMock {
    public static List<ChargeEntity> data(UserEntity originator1, UserEntity originator2, UserEntity recipient1, UserEntity recipient2) {
        return Arrays.asList(
                ChargeEntity.builder()
                        .amount(new BigDecimal("150.75"))
                        .createdAt(LocalDateTime.now().minusDays(3))
                        .status(Status.PENDING)
                        .originatorUser(originator1)
                        .recipientUser(recipient1)
                        .build(),
                ChargeEntity.builder()
                        .amount(new BigDecimal("300.00"))
                        .createdAt(LocalDateTime.now().minusDays(2))
                        .status(Status.PAID)
                        .originatorUser(originator2)
                        .recipientUser(recipient2)
                        .build(),

                ChargeEntity.builder()
                        .amount(new BigDecimal("89.99"))
                        .description("Transfer for subscription")
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .status(Status.PAID)
                        .originatorUser(originator1)
                        .recipientUser(recipient2)
                        .build(),
                ChargeEntity.builder()
                        .amount(new BigDecimal("500.50"))
                        .description("Payment")
                        .createdAt(LocalDateTime.now())
                        .status(Status.PENDING)
                        .originatorUser(originator2)
                        .recipientUser(recipient1)
                        .build()
        );
    }
}
