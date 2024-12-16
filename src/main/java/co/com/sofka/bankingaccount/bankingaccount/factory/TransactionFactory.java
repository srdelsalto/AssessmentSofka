package co.com.sofka.bankingaccount.bankingaccount.factory;

import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.impl.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class TransactionFactory {
    private final Map<String, BiFunction<BigDecimal, String, TransactionBase>> transactionStrategy = new HashMap<>();

    public TransactionFactory() {
        // Strategy Registry
        transactionStrategy.put("ATM_DEPOSIT", AtmDeposit::new);
        transactionStrategy.put("ATM_WITHDRAWAL", AtmWithdrawal::new);
        transactionStrategy.put("BRANCH_DEPOSIT", BranchDeposit::new);
        transactionStrategy.put("CARD_PHYSICAL_BUY", CardPhysicalBuy::new);
        transactionStrategy.put("CARD_WEB_BUY", CardWebBuy::new);
        transactionStrategy.put("DEPOSIT_FROM_ANOTHER_ACCOUNT", DepositFromAnotherAccount::new);
    }

    public TransactionBase createTransaction(String type, BigDecimal amount, String accountId){
        BiFunction<BigDecimal, String, TransactionBase> strategy = transactionStrategy.get(type.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Transaction Type not valid: " + type);
        }

        return strategy.apply(amount, accountId);
    }
}
