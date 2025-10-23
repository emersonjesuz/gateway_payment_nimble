package com.nimble.gateway_payment.accountBank;

import com.nimble.gateway_payment.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountBankRepository extends JpaRepository<AccountBankEntity, UUID> {
    Optional<AccountBankEntity> findByUsers(UserEntity user);
}
