-- Seed demo user (fixed UUID so services/tests can reference consistently)
INSERT INTO users (id, username, created_at)
VALUES ('11111111-1111-1111-1111-111111111111', 'demo', CURRENT_TIMESTAMP);

-- Seed wallet balances for demo user
-- Note: 'id' column omitted, DB auto-generates it
INSERT INTO wallet_balance (user_id, asset, balance, version)
VALUES ('11111111-1111-1111-1111-111111111111', 'USDT', 50000, 0),
       ('11111111-1111-1111-1111-111111111111', 'BTC', 0, 0),
       ('11111111-1111-1111-1111-111111111111', 'ETH', 0, 0);