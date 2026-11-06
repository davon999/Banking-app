package ro.david;

import ro.david.exception.InsufficientFundsException;

import java.math.BigDecimal;

public class Account {
    private BigDecimal balance;
    private final Currency currency;
    private AccountStatus status;
    private final String id;
    private final String iban;
    private final String ownerName;

    public enum AccountStatus {
        ACTIVE,
        BLOCKED,
        CLOSED;
    }

    public Account(String id, String iban, String ownerName, Currency currency) {
        this.id = id;
        this.iban = iban;
        this.ownerName = ownerName;
        this.currency = currency;
        this.balance = BigDecimal.ZERO;
        this.status = AccountStatus.ACTIVE;
    }

    public String getId() {
        return id;
    }

    public String getIban() {
        return iban;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void deposit(BigDecimal value) {
        if (this.status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Your account is currently: " + this.status);
        }
        if (value == null) {
            throw new IllegalArgumentException("Deposit amount must not be null");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("You cannot deposit " + value);
        }
        this.balance = this.balance.add(value);
    }

    public void withdraw(BigDecimal value) {
        if (this.status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Your account is currently: " + this.status);
        }

        if (value == null) {
            throw new IllegalArgumentException("Withdrawal amount must not be null");
        }
        if (this.balance.compareTo(value) < 0) {
            throw new InsufficientFundsException("Insufficient funds: requested " + value + ", available " + this.balance);
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("You cannot withdraw " + value);
        }
        this.balance = this.balance.subtract(value);
    }
}


