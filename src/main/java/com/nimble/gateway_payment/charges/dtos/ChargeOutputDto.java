package com.nimble.gateway_payment.charges.dtos;

import com.nimble.gateway_payment.charges.ChargeEntity;
import com.nimble.gateway_payment.charges.enums.Status;
import com.nimble.gateway_payment.user.UserEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ChargeOutputDto {
    private UUID id;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;
    private Status status;
    private ChargeUserOutput originatorUser;
    private ChargeUserOutput recipientUser;

    public ChargeOutputDto(ChargeEntity charge) {
        this.id = charge.getId();
        this.amount = charge.getAmount();
        this.description = charge.getDescription();
        this.createdAt = charge.getCreatedAt();
        this.status = charge.getStatus();
        this.originatorUser = new ChargeUserOutput(charge.getOriginatorUser());
        this.recipientUser = new ChargeUserOutput(charge.getRecipientUser());
    }
}

@Getter
class ChargeUserOutput {
    private UUID id;
    private String name;
    private String cpf;

    public ChargeUserOutput(UserEntity user) {
        this.id = user.getId();
        this.name = user.getName();
        this.cpf = user.getCpf();
    }
}



