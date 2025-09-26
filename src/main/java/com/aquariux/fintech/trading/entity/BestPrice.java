package com.aquariux.fintech.trading.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    name = "best_price",
    uniqueConstraints = @UniqueConstraint(columnNames = {"base_asset", "quote_asset"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BestPrice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Asset baseAsset;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Asset quoteAsset;

  @Column(precision = 38, scale = 10)
  private BigDecimal bestBid; // highest buy price

  @Enumerated(EnumType.STRING)
  private Exchange bestBidSource;

  @Column(precision = 38, scale = 10)
  private BigDecimal bestAsk; // lowest sell price

  @Enumerated(EnumType.STRING)
  private Exchange bestAskSource;

  @UpdateTimestamp
  private Instant asOf;
}
