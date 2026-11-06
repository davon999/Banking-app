CREATE TABLE accounts
(
    id         VARCHAR(36) PRIMARY KEY,
    iban       VARCHAR(34)    NOT NULL UNIQUE,
    owner_name VARCHAR(100)   NOT NULL,
    currency   VARCHAR(3)     NOT NULL,
    balance    NUMERIC(19, 2) NOT NULL DEFAULT 0 CHECK (balance >= 0),
    status     VARCHAR(20)    NOT NULL
);