package co.com.sofka.bankingaccount.bankingaccount.model.transaction.impl;

import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "transactions")
@NoArgsConstructor
public class AtmDeposit extends TransactionBase {
    public AtmDeposit(BigDecimal amount, String accountId){
        super(amount, accountId, "ATM_DEPOSIT");
        this.setCost(BigDecimal.valueOf(2)); //Established Cost 2$
    }

    @Override
    public BigDecimal calculateImpact(){
        return getAmount().subtract(getCost());
    }
}
