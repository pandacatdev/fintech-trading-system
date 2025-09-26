# Simplified Fintech Trading System

## Project Overview

This is a simplified trading system for BTC/USDT and ETH/USDT.
It periodically fetches prices from Binance and Huobi, stores the best aggregated bid/ask prices, and allows a demo user to place trades.

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA (Hibernate)**
- **H2 (in-memory DB for assignment)**

## Assumptions & Simplifications

- Only BTC/USDT and ETH/USDT pairs supported.
- Quantities from Binance/Huobi (bidQty, askQty) are ignored. Only prices are used.
- Single demo user (UUID fixed in controller). In a real system, userId would come from Spring Security.
- Trades are synchronous. In production, they would likely be handled asynchronously via a queue.

## Data Model

- **User** → Demo user only.
- **WalletBalance** → Tracks balance per asset.
- **BestPrice** → Latest aggregated bid/ask per trading pair.
- **Trade** → Immutable record of executed trade.

## How to Run
### Prerequisites

- Java 21

### Run the app
```
./gradlew bootRun
```

### H2 Console

- **URL**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL**: `jdbc:h2:mem:trade_db`
- **User**: `sa`
- **Password**: (empty)

## Features Implemented

### Scheduler
- Fetches Binance/Huobi every 10s, stores best bid/ask.

### API Endpoints
#### Prices

- `GET /api/prices`: All best prices
- `GET /api/prices/{base}/{quote}`: Specific pair

#### Wallet

- `GET /api/wallets/me`: Demo user balances

#### Trades

- `POST /api/trades`

```json
{
  "symbol": "BTCUSDT",
  "side": "BUY",
  "quantity": 0.1
}
```

- `GET /api/trades`: Trade history

## Example Usage (curl)
# Check wallet
```bash
curl -s http://localhost:8080/api/wallets/me | jq
```

# Place a trade
```bash
curl -X POST http://localhost:8080/api/trades \
-H "Content-Type: application/json" \
-d '{"symbol":"BTCUSDT","side":"BUY","quantity":0.1}' | jq
```

# Get trade history
```bash
curl -s http://localhost:8080/api/trades | jq
```

## Future Improvements

- Add Spring Security + JWT for real user handling.
- Support more trading pairs dynamically.
- Use async trade execution with message queue.
- Improve error handling (@RestControllerAdvice).
