package co.com.sofka.bankingaccount.bankingaccount.model.transaction.impl;

import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "transactions")
@NoArgsConstructor
public class CardWebBuy extends TransactionBase {
    public CardWebBuy (BigDecimal amount, String accountId){
        super(amount.multiply(BigDecimal.valueOf(-1)), accountId, "CARD_WEB_BUY");
        this.setCost(BigDecimal.valueOf(5)); //Value for secure
    }

    @Override
    public BigDecimal calculateImpact(){
        return getAmount().subtract(getCost());
    }
}
