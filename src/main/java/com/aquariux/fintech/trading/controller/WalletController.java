package com.aquariux.fintech.trading.controller;

import com.aquariux.fintech.trading.dto.WalletResponse;
import com.aquariux.fintech.trading.service.WalletService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {
  private final WalletService walletService;

  // Assume the user has been authenticated and we have their userId
  private static final UUID DEMO_USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

  @GetMapping("/me")
  public List<WalletResponse> getMyWallet() {
    return walletService.getWalletBalances(DEMO_USER_ID);
  }
}
