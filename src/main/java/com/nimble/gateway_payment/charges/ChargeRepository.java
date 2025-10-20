package com.nimble.gateway_payment.charges;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChargeRepository extends JpaRepository<ChargeEntity, UUID> {
}
