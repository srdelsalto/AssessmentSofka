package co.com.sofka.bankingaccount.bankingaccount.repository;

import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<TransactionBase, String> {
    List<TransactionBase> findByAccountId(String accountId);
}
