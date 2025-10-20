package com.nimble.gateway_payment.charges;

import com.nimble.gateway_payment.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "charges")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "originator_cpf", nullable = false)
    private UserEntity originatorUser;

    @ManyToOne
    @JoinColumn(name = "recipient_cpf", nullable = false)
    private UserEntity recipientUser;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    private String description;
}
