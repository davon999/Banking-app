package ro.david;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

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
}
