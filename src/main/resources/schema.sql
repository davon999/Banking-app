CREATE TABLE accounts
(
    id         VARCHAR(36) PRIMARY KEY,
    iban       VARCHAR(34)    NOT NULL UNIQUE,
    owner_name VARCHAR(100)   NOT NULL,
    currency   VARCHAR(3)     NOT NULL,
    balance    NUMERIC(19, 2) NOT NULL DEFAULT 0 CHECK (balance >= 0),
    status     VARCHAR(20)    NOT NULL
);

CREATE TABLE transactions
(
    id         BIGSERIAL PRIMARY KEY,
    account_id VARCHAR(36)    NOT NULL REFERENCES accounts (id),
    type       VARCHAR(20)    NOT NULL,
    amount     NUMERIC(19, 2) NOT NULL CHECK (amount > 0),
    created_at TIMESTAMP      NOT NULL DEFAULT NOW()
);