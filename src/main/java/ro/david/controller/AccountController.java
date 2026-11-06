package ro.david.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.david.Account;
import ro.david.Currency;
import ro.david.service.AccountService;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    public record OpenAccountRequest(String ownerName, Currency currency) {}
    public record AmountRequest(BigDecimal amount) {}
    public record TransferRequest(String fromIban, String toIban, BigDecimal amount) {}

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Account openAccount(@RequestBody OpenAccountRequest request) {
        return accountService.openAccount(request.ownerName(), request.currency());
    }

    @GetMapping("/{iban}")
    public Account getAccount(@PathVariable String iban) {
        return accountService.findAccount(iban);
    }

    @PostMapping("/{iban}/deposit")
    public Account deposit(@PathVariable String iban, @RequestBody AmountRequest request) {
        return accountService.deposit(iban, request.amount());
    }

    @PostMapping("/{iban}/withdraw")
    public Account withdraw(@PathVariable String iban, @RequestBody AmountRequest request) {
        return accountService.withdraw(iban, request.amount());
    }

    @PostMapping("/transfer")
    public Map<String, String> transfer(@RequestBody TransferRequest request) {
        accountService.transfer(request.fromIban(), request.toIban(), request.amount());
        return Map.of("message", "Transfer completed");
    }
}