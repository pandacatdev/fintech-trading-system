package com.aquariux.fintech.trading.repository;

import com.aquariux.fintech.trading.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {

}
