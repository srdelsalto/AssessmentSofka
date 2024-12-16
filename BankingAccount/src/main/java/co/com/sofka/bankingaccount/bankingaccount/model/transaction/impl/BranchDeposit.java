package co.com.sofka.bankingaccount.bankingaccount.model.transaction.impl;

import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "transactions")
@NoArgsConstructor
public class BranchDeposit extends TransactionBase {
    public BranchDeposit(BigDecimal amount, String accountId){
        super(amount,accountId,"BRANCH_DEPOSIT");
        this.setCost(BigDecimal.ZERO); //Without Cost
    }

    @Override
    public BigDecimal calculateImpact(){
        return getAmount();
    }
}
