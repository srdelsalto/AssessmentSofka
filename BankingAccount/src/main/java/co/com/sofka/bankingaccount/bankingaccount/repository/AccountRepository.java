package co.com.sofka.bankingaccount.bankingaccount.repository;

import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<BankAccount, String> {
}
