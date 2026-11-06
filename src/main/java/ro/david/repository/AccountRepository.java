package ro.david.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.david.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByIban(String iban);
}