package com.aquariux.fintech.trading.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "trade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trade {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Asset baseAsset;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Asset quoteAsset;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private TradeSide side;

  @Column(precision = 38, scale = 10, nullable = false)
  private BigDecimal price;

  @Column(precision = 38, scale = 10, nullable = false)
  private BigDecimal quantity;

  @Column(precision = 38, scale = 10, nullable = false)
  private BigDecimal quoteAmount;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Instant executedAt;
}
