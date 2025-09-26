package com.aquariux.fintech.trading.service;

import com.aquariux.fintech.trading.dto.TradeRequest;
import com.aquariux.fintech.trading.dto.TradeResponse;
import com.aquariux.fintech.trading.entity.Asset;
import com.aquariux.fintech.trading.entity.BestPrice;
import com.aquariux.fintech.trading.entity.Trade;
import com.aquariux.fintech.trading.entity.TradeSide;
import com.aquariux.fintech.trading.entity.User;
import com.aquariux.fintech.trading.entity.WalletBalance;
import com.aquariux.fintech.trading.repository.BestPriceRepository;
import com.aquariux.fintech.trading.repository.TradeRepository;
import com.aquariux.fintech.trading.repository.WalletBalanceRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeService {
  private final BestPriceRepository bestPriceRepository;
  private final WalletBalanceRepository walletBalanceRepository;
  private final TradeRepository tradeRepository;

  @Transactional(rollbackFor = Exception.class)
  public TradeResponse executeTrade(UUID userId, TradeRequest request) {
    // For simplicity, we only support trading pairs ETHUSDT, BTCUSDT
    String baseStr = request.getSymbol().replace("USDT", "");
    Asset baseAsset = Asset.valueOf(baseStr);
    Asset quoteAsset = Asset.USDT;

    // Fetch the best price
    BestPrice bestPrice = bestPriceRepository.findByBaseAssetAndQuoteAsset(baseAsset, quoteAsset)
        .orElseThrow(() -> new IllegalArgumentException("No price available for " + request.getSymbol()));
    BigDecimal price = (request.getSide() == TradeSide.BUY)
        ? bestPrice.getBestAsk()
        : bestPrice.getBestBid();
    BigDecimal quantity = request.getQuantity();
    BigDecimal quoteAmount = price.multiply(quantity);

    // Update wallet balances
    WalletBalance baseWallet = walletBalanceRepository
        .findByUserIdAndAsset(userId, baseAsset)
        .orElseThrow(() -> new IllegalStateException("Wallet not found for base asset " + baseAsset));

    WalletBalance quoteWallet = walletBalanceRepository
        .findByUserIdAndAsset(userId, quoteAsset)
        .orElseThrow(() -> new IllegalStateException("Wallet not found for quote asset " + quoteAsset));

    if (request.getSide() == TradeSide.BUY) {
      if (quoteWallet.getBalance().compareTo(quoteAmount) < 0) {
        throw new IllegalArgumentException("Insufficient USDT balance");
      }
      quoteWallet.setBalance(quoteWallet.getBalance().subtract(quoteAmount));
      baseWallet.setBalance(baseWallet.getBalance().add(quantity));
    } else {
      if (baseWallet.getBalance().compareTo(quantity) < 0) {
        throw new IllegalArgumentException("Insufficient " + baseAsset + " balance");
      }
      baseWallet.setBalance(baseWallet.getBalance().subtract(quantity));
      quoteWallet.setBalance(quoteWallet.getBalance().add(quoteAmount));
    }

    walletBalanceRepository.save(baseWallet);
    walletBalanceRepository.save(quoteWallet);

    Trade trade = Trade.builder()
        .user(User.builder().id(userId).build())
        .side(request.getSide())
        .baseAsset(baseAsset)
        .quoteAsset(quoteAsset)
        .price(price)
        .quantity(quantity)
        .quoteAmount(quoteAmount)
        .build();

    tradeRepository.save(trade);

    return TradeResponse.builder()
        .symbol(request.getSymbol())
        .side(request.getSide())
        .price(price)
        .quantity(quantity)
        .quoteAmount(quoteAmount)
        .executedAt(trade.getExecutedAt())
        .build();
  }

  public List<TradeResponse> getTradeHistory(UUID userId) {
    return tradeRepository.findByUserIdOrderByExecutedAtDesc(userId).stream()
        .map(trade -> TradeResponse.builder()
            .symbol(trade.getBaseAsset().name() + trade.getQuoteAsset().name())
            .side(trade.getSide())
            .price(trade.getPrice())
            .quantity(trade.getQuantity())
            .quoteAmount(trade.getQuoteAmount())
            .executedAt(trade.getExecutedAt())
            .build())
        .toList();
  }

}
