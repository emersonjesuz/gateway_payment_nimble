package com.nimble.gateway_payment;

import com.nimble.gateway_payment.charges.ChargeEntity;
import com.nimble.gateway_payment.charges.Status;
import com.nimble.gateway_payment.user.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ChargeListToMock {
    public static List<ChargeEntity> data() {
        return Arrays.asList(
                ChargeEntity.builder()
                        .id(UUID.randomUUID())
                        .amount(new BigDecimal("100.50"))
                        .description("Payment for order #1001")
                        .createdAt(LocalDateTime.now().minusDays(2))
                        .status(Status.PENDING)
                        .originatorUser(UserEntity.builder().build())
                        .recipientUser(UserEntity.builder().build())
                        .build(),

                ChargeEntity.builder()
                        .id(UUID.randomUUID())
                        .amount(new BigDecimal("250.00"))
                        .description("Transfer to user B")
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .status(Status.PAID)
                        .originatorUser(UserEntity.builder().build())
                        .recipientUser(UserEntity.builder().build())
                        .build(),

                ChargeEntity.builder()
                        .id(UUID.randomUUID())
                        .amount(new BigDecimal("75.30"))
                        .description("Refund for order #982")
                        .createdAt(LocalDateTime.now())
                        .status(Status.CANCELED)
                        .originatorUser(UserEntity.builder().build())
                        .recipientUser(UserEntity.builder().build())
                        .build()
        );
    }
}
