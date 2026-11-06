package ro.david;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import ro.david.exception.InsufficientFundsException;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private String id;
    private String iban;
    private String ownerName;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public enum AccountStatus {
        ACTIVE,
        BLOCKED,
        CLOSED
    }

    protected Account() {
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
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("You cannot withdraw " + value);
        }
        if (this.balance.compareTo(value) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds: requested " + value + ", available " + this.balance);
        }
        this.balance = this.balance.subtract(value);
    }
}