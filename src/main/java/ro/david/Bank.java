package ro.david;

import ro.david.exception.AccountNotFoundException;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.math.BigDecimal;

public class Bank {
    private final Map<String, Account> accounts = new HashMap<>();
    private long nextAccountNumber = 1;

    public Account openAccount(String ownerName, Currency currency) {
        String id = UUID.randomUUID().toString();
        String iban = String.format("RO49DAVB%010d", nextAccountNumber);
        nextAccountNumber++;
        Account createdAccount = new Account(id, iban, ownerName, currency);
        accounts.put(iban, createdAccount);
        return createdAccount;
    }

    public Account findAccount(String iban) {
        Account account = accounts.get(iban);
        if (account == null) {
            throw new AccountNotFoundException("No account has been found with IBAN " + iban);
        }
        return account;
    }

    public void transfer(String fromIban, String toIban, BigDecimal amount) {
        if (fromIban.equals(toIban)) {
            throw new IllegalArgumentException("Cannot transfer to the same account: " + fromIban);
        }

        Account from = findAccount(fromIban);
        Account to = findAccount(toIban);

        if (from.getCurrency() != to.getCurrency()) {
            throw new IllegalArgumentException(
                    "Currency mismatch: " + from.getCurrency() + " -> " + to.getCurrency());
        }

        from.withdraw(amount);
        to.deposit(amount);
    }
}
