package co.com.sofka.bankingaccount.bankingaccount.model.transaction.impl;

import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "transactions")
@NoArgsConstructor
public class DepositFromAnotherAccount extends TransactionBase {

    public DepositFromAnotherAccount(BigDecimal amount, String accountId) {
        super(amount, accountId, "DEPOSIT_FROM_ANOTHER_ACCOUNT");
        this.setCost(BigDecimal.valueOf(1.5));
    }

    @Override
    public BigDecimal calculateImpact() {
        return getAmount().subtract(getCost());
    }
}
