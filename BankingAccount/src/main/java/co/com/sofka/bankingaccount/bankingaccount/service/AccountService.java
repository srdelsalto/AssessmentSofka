package co.com.sofka.bankingaccount.bankingaccount.service;

import co.com.sofka.bankingaccount.bankingaccount.application.dto.TransactionDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.request.CreateAccountRequestDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.response.AccountResponseDTO;
import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import co.com.sofka.bankingaccount.bankingaccount.repository.AccountRepository;
import co.com.sofka.bankingaccount.bankingaccount.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public BankAccount createAccount(CreateAccountRequestDTO createAccountRequestDTO){
        if (createAccountRequestDTO.getEntitledUserName() == null){
            throw new NullPointerException();
        }

        BankAccount createdAccount = new BankAccount();
        createdAccount.setBalance(createAccountRequestDTO.getBalance());
        createdAccount.setEntitledUser(createAccountRequestDTO.getEntitledUserName());

        return accountRepository.save(createdAccount);
    }

    public BankAccount getAccount(String id){
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not founded!"));
    }

    public BigDecimal executeTransaction(String accountId, TransactionBase transaction){
        BankAccount account = getAccount(accountId);

        BigDecimal impact = transaction.calculateImpact();
        account.applyTransaction(impact);

        transaction.setAccountId(accountId);
        transactionRepository.save(transaction);
        accountRepository.save(account);

        return account.getBalance();
    }

    public List<TransactionBase> getTransactionsByAccount(String accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public AccountResponseDTO getAccountWithTransaction(String id) {
        BankAccount cuenta = getAccount(id);

        List<TransactionBase> transacciones = new ArrayList<>();

        transacciones = getTransactionsByAccount(cuenta.getId());

        List<TransactionDTO> transactionDTOS = new ArrayList<>();

        if (!transacciones.isEmpty()){
            for (int i = 0; i < transacciones.size(); i++){
                TransactionDTO transactionDTO = new TransactionDTO(transacciones.get(i).getId(), transacciones.get(i).getType(), transacciones.get(i).getAmount(), transacciones.get(i).getCost() );
                transactionDTOS.add(transactionDTO);
            }
        }

        return new AccountResponseDTO(cuenta.getId(), cuenta.getBalance(), transactionDTOS);
    }
}
