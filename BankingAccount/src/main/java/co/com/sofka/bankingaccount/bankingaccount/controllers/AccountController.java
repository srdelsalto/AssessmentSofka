package co.com.sofka.bankingaccount.bankingaccount.controllers;

import co.com.sofka.bankingaccount.bankingaccount.application.dto.request.CreateAccountRequestDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.request.TransactionRequestDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.response.AccountResponseDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.response.AccountResponseTransactionDTO;
import co.com.sofka.bankingaccount.bankingaccount.factory.TransactionFactory;
import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import co.com.sofka.bankingaccount.bankingaccount.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/bankAccount")
public class AccountController {
    private final AccountService accountService;
    private final TransactionFactory factory;

    public AccountController(AccountService accountService, TransactionFactory transactionFactory){
        this.accountService = accountService;
        this.factory = transactionFactory;
    }

    @PostMapping
    public BankAccount createBankAccount(@RequestBody @Valid CreateAccountRequestDTO createAccountRequestDTO){
        return accountService.createAccount(createAccountRequestDTO);
    }

    @PostMapping("/transaction")
    public ResponseEntity<AccountResponseTransactionDTO> executeTransaction(@RequestBody @Valid TransactionRequestDTO transactionDTO){
        BankAccount account = accountService.getAccount(transactionDTO.getId());

        // Crear la transacción usando el patrón de estrategia
        TransactionBase transaccion = factory.createTransaction(
                transactionDTO.getType(),
                transactionDTO.getAmount(),
                transactionDTO.getId()
        );

        BigDecimal saldoActualizado = accountService.executeTransaction(account.getId(), transaccion);
        return new ResponseEntity<>(new AccountResponseTransactionDTO(account.getId(),saldoActualizado), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<AccountResponseDTO> getAccountWithTransactions(@RequestParam("id") String id) {
        return new ResponseEntity<>(accountService.getAccountWithTransaction(id), HttpStatus.OK);
    }
}
