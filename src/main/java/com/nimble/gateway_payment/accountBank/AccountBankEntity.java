package com.nimble.gateway_payment.accountBank;

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
@Table(name = "account_bank")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBankEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "cpf", referencedColumnName = "cpf", nullable = false, unique = true)
    private UserEntity users;
}
