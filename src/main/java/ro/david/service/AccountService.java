package ro.david.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.david.Account;
import ro.david.Currency;
import ro.david.exception.AccountNotFoundException;
import ro.david.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account openAccount(String ownerName, Currency currency) {
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Owner name must not be empty");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency must not be null");
        }
        String id = UUID.randomUUID().toString();
        String iban = String.format("RO49DAVB%010d", accountRepository.count() + 1);
        return accountRepository.save(new Account(id, iban, ownerName, currency));
    }

    public Account findAccount(String iban) {
        return accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException("No account has been found with IBAN " + iban));
    }

    @Transactional
    public Account deposit(String iban, BigDecimal amount) {
        Account account = findAccount(iban);
        account.deposit(amount);
        return accountRepository.save(account);
    }

    @Transactional
    public Account withdraw(String iban, BigDecimal amount) {
        Account account = findAccount(iban);
        account.withdraw(amount);
        return accountRepository.save(account);
    }

    @Transactional
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
        accountRepository.save(from);
        accountRepository.save(to);
    }
}