package co.com.sofka.bankingaccount.bankingaccount.model.transaction.impl;

import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "transactions")
@NoArgsConstructor
public class AtmWithdrawal extends TransactionBase {
    public AtmWithdrawal(BigDecimal amount, String accountId){
        super(amount.multiply(BigDecimal.valueOf(-1)), accountId, "ATM_WITHDRAWAL");
        this.setCost(BigDecimal.ONE); //Established Cost
    }

    @Override
    public BigDecimal calculateImpact(){
        return getAmount().subtract(getCost());
    }
}
