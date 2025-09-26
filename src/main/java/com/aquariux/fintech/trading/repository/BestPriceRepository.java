package com.aquariux.fintech.trading.repository;

import com.aquariux.fintech.trading.entity.Asset;
import com.aquariux.fintech.trading.entity.BestPrice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestPriceRepository extends JpaRepository<BestPrice, Long> {
  Optional<BestPrice> findByBaseAssetAndQuoteAsset(Asset baseAsset, Asset quoteAsset);
}
