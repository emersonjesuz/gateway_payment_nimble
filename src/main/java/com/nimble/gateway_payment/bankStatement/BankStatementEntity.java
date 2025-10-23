package com.nimble.gateway_payment.bankStatement;

import com.nimble.gateway_payment.accountBank.TypePayment;
import com.nimble.gateway_payment.charges.ChargeEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bank_statement")
public class BankStatementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "charge_id", nullable = false, unique = true)
    private ChargeEntity charge;

    @Column(name = "type_payment", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypePayment typePayment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}