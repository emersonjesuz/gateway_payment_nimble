package com.nimble.gateway_payment.bankStatement;

import com.nimble.gateway_payment.charges.ChargeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankStatementRepository extends JpaRepository<BankStatementEntity, UUID> {
    Optional<BankStatementEntity> findByCharge(ChargeEntity charge);
}
