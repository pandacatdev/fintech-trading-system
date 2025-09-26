package com.aquariux.fintech.trading.controller;

import com.aquariux.fintech.trading.dto.TradeRequest;
import com.aquariux.fintech.trading.dto.TradeResponse;
import com.aquariux.fintech.trading.service.TradeService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeController {
  private final TradeService tradeService;

  // Assume the user has been authenticated and we have their userId
  private static final UUID DEMO_USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

  @PostMapping
  public ResponseEntity<TradeResponse> placeTrade(@RequestBody @Valid TradeRequest request) {
    TradeResponse response = tradeService.executeTrade(DEMO_USER_ID, request);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public List<TradeResponse> getTradeHistory() {
    return tradeService.getTradeHistory(DEMO_USER_ID);
  }
}
