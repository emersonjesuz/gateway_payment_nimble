package com.nimble.gateway_payment.charges;

import com.nimble.gateway_payment.charges.enums.Status;
import com.nimble.gateway_payment.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChargeRepository extends JpaRepository<ChargeEntity, UUID> {
    @Query("SELECT c FROM ChargeEntity c WHERE c.originatorUser = :user AND (:status IS NULL OR c.status = :status) ORDER BY c.createdAt DESC")
    List<ChargeEntity> findAllByOriginatorUser(@Param("user") UserEntity user, @Param("status") Status status);

    @Query("SELECT c FROM ChargeEntity c WHERE c.recipientUser = :user AND (:status IS NULL OR c.status = :status) ORDER BY c.createdAt DESC")
    List<ChargeEntity> findAllByRecipientUser(@Param("user") UserEntity user, @Param("status") Status status);

    Optional<ChargeEntity> findByIdAndStatusAndRecipientUser(UUID uuid, Status status, UserEntity user);
}
