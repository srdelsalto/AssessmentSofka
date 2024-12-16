package co.com.sofka.bankingaccount.bankingaccount.model.transaction.impl;

import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "transactions")
@NoArgsConstructor
public class CardPhysicalBuy extends TransactionBase {
    public CardPhysicalBuy(BigDecimal amount, String accountId){
        super(amount, accountId, "CARD_PHYSICAL_BUY");
        this.setCost(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal calculateImpact(){
        return getAmount();
    }
}
