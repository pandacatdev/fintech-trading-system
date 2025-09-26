#!/bin/bash
# End-to-end test script for Simplified Fintech Trading System
# Usage: bash test-e2e.sh

BASE_URL="http://localhost:8080/api"

# Start Spring Boot app in background with logs suppressed
./gradlew bootRun > /dev/null 2>&1 &
APP_PID=$!

echo "Waiting for application to start..."
for i in {1..30}; do
  if curl -s $BASE_URL/wallets/me > /dev/null; then
    echo "Application is up!"
    break
  fi
  sleep 2
done

if ! kill -0 $APP_PID 2>/dev/null; then
  echo "Spring Boot app failed to start."
  exit 1
fi

echo "Waiting 10 seconds for data feed scheduler..."
sleep 10

echo "=== Check Wallet ==="
WALLET_JSON=$(curl -s $BASE_URL/wallets/me)
echo "Wallet response:" && echo "$WALLET_JSON" | jq
BALANCE=$(echo "$WALLET_JSON" | jq '.[] | select(.asset=="USDT") | .balance')
if [ -z "$BALANCE" ]; then
  echo "USDT balance is missing!"
  kill $APP_PID
  exit 1
else
  echo "USDT balance is available: $BALANCE"
fi

echo "=== Check Prices ==="
PRICES_JSON=$(curl -s $BASE_URL/prices)
echo "Prices response:" && echo "$PRICES_JSON" | jq
BTC_PRICE=$(echo "$PRICES_JSON" | jq '.[] | select(.baseAsset=="BTC" and .quoteAsset=="USDT") | .bestAsk')
ETH_PRICE=$(echo "$PRICES_JSON" | jq '.[] | select(.baseAsset=="ETH" and .quoteAsset=="USDT") | .bestAsk')
if [ -z "$BTC_PRICE" ]; then
  echo "BTCUSDT price is missing!"
  kill $APP_PID
  exit 1
else
  echo "BTCUSDT price is available: $BTC_PRICE"
fi

if [ -z "$ETH_PRICE" ]; then
  echo "ETHUSDT price is missing!"
  kill $APP_PID
  exit 1
else
  echo "ETHUSDT price is available: $ETH_PRICE"
fi

echo "=== Buy 0.1 BTC ==="
TRADE_BUY_JSON=$(curl -s -X POST $BASE_URL/trades \
  -H "Content-Type: application/json" \
  -d '{"symbol":"BTCUSDT","side":"BUY","quantity":0.1}')
echo "Buy trade response:" && echo "$TRADE_BUY_JSON" | jq
TRADE_BUY_STATUS=$(echo "$TRADE_BUY_JSON" | jq -r '.side')
if [[ "$TRADE_BUY_STATUS" == "BUY" ]]; then
  echo "Buy trade succeeded."
else
  echo "Buy trade failed!"
  kill $APP_PID
  exit 1
fi

echo "=== Sell 0.05 BTC ==="
TRADE_SELL_JSON=$(curl -s -X POST $BASE_URL/trades \
  -H "Content-Type: application/json" \
  -d '{"symbol":"BTCUSDT","side":"SELL","quantity":0.05}')
echo "Sell trade response:" && echo "$TRADE_SELL_JSON" | jq
TRADE_SELL_STATUS=$(echo "$TRADE_SELL_JSON" | jq -r '.side')
if [[ "$TRADE_SELL_STATUS" == "SELL" ]]; then
  echo "Sell trade succeeded."
else
  echo "Sell trade failed!"
  kill $APP_PID
  exit 1
fi

echo "=== Buy 1 ETH ==="
TRADE_ETH_JSON=$(curl -s -X POST $BASE_URL/trades \
  -H "Content-Type: application/json" \
  -d '{"symbol":"ETHUSDT","side":"BUY","quantity":1}')
echo "ETH buy trade response:" && echo "$TRADE_ETH_JSON" | jq
TRADE_ETH_STATUS=$(echo "$TRADE_ETH_JSON" | jq -r '.side')
if [[ "$TRADE_ETH_STATUS" == "BUY" ]]; then
  echo "ETH buy trade succeeded."
else
  echo "ETH buy trade failed!"
  kill $APP_PID
  exit 1
fi

echo "=== Check Wallet Again ==="
WALLET_JSON_RECHECK=$(curl -s $BASE_URL/wallets/me)
echo "Wallet after trades:" && echo "$WALLET_JSON_RECHECK" | jq

echo "=== Get Trade History ==="
TRADE_HISTORY_JSON=$(curl -s $BASE_URL/trades)
echo "Trade history response:" && echo "$TRADE_HISTORY_JSON" | jq
TRADE_HISTORY_COUNT=$(echo "$TRADE_HISTORY_JSON" | jq 'length')
if (( TRADE_HISTORY_COUNT == 3 )); then
  echo "Trade history contains 3 trades."
else
  echo "Trade history does not contain expected trades!"
  kill $APP_PID
  exit 1
fi

echo "All E2E tests passed!"
kill $APP_PID
