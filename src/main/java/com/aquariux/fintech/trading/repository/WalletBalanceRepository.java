package com.aquariux.fintech.trading.repository;

import com.aquariux.fintech.trading.entity.WalletBalance;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletBalanceRepository extends JpaRepository<WalletBalance, Long> {
  List<WalletBalance> findByUserId(UUID userId);
}