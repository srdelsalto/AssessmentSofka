package co.com.sofka.bankingaccount.bankingaccount.model;

import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
public class BankAccount {
    @Id
    private String id;

    private String entitledUser;

//    private String accountNumber;

    private BigDecimal balance;

    @DBRef(lazy = true)
    private List<TransactionBase> transactions;

    public BankAccount(String id, String entitledUser, BigDecimal balance){
        this.id = id;
        this.entitledUser = entitledUser;
        this.balance = balance;
    }

    public void applyTransaction(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
}
