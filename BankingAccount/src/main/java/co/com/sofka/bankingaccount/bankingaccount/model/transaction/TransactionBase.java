package co.com.sofka.bankingaccount.bankingaccount.model.transaction;

import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "transactions")
@Getter
@Setter
@NoArgsConstructor
public abstract class TransactionBase {
    @Id
    private String id;

    private BigDecimal amount;
    private BigDecimal cost;
    private String type; //Added for MongoDB

    @Indexed
    private String accountId;

    public TransactionBase(BigDecimal amount, String accountId, String type) {
        this.amount = amount;
        this.accountId = accountId;
        this.type = type;
    }

    public abstract BigDecimal calculateImpact();
}
