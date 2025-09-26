package com.aquariux.fintech.trading.service;

import com.aquariux.fintech.trading.dto.WalletResponse;
import com.aquariux.fintech.trading.repository.WalletBalanceRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {
  private final WalletBalanceRepository walletBalanceRepository;

  public List<WalletResponse> getWalletBalances(UUID userId) {
    return walletBalanceRepository.findByUserId(userId).stream()
        .map(w -> WalletResponse.builder()
            .asset(w.getAsset())
            .balance(w.getBalance())
            .build())
        .collect(Collectors.toList());
  }
}
