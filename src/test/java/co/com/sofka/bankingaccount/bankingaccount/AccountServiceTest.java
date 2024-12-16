package co.com.sofka.bankingaccount.bankingaccount;

import co.com.sofka.bankingaccount.bankingaccount.application.dto.request.CreateAccountRequestDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.response.AccountResponseDTO;
import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.impl.BranchDeposit;
import co.com.sofka.bankingaccount.bankingaccount.repository.AccountRepository;
import co.com.sofka.bankingaccount.bankingaccount.repository.TransactionRepository;
import co.com.sofka.bankingaccount.bankingaccount.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private BankAccount mockAccount;
    private TransactionBase mockTransaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuración de datos simulados
        mockAccount = new BankAccount();
        mockAccount.setId(UUID.randomUUID().toString());
        mockAccount.setBalance(BigDecimal.valueOf(1000));
        mockAccount.setEntitledUser("Test User");

        mockTransaction = new TransactionBase() {
            @Override
            public BigDecimal calculateImpact() {
                return BigDecimal.valueOf(-200);
            }
        };
        mockTransaction.setId(UUID.randomUUID().toString());
        mockTransaction.setAccountId(mockAccount.getId());
        mockTransaction.setAmount(BigDecimal.valueOf(-200));
        mockTransaction.setCost(BigDecimal.valueOf(5));
        mockTransaction.setType("WITHDRAW");
    }

    @Test
    void testCreateAccount() {
        // Configurar datos simulados
        CreateAccountRequestDTO requestDTO = new CreateAccountRequestDTO();
        requestDTO.setBalance(BigDecimal.valueOf(500));
        requestDTO.setEntitledUserName("John Doe");

        BankAccount savedAccount = new BankAccount();
        savedAccount.setId(UUID.randomUUID().toString());
        savedAccount.setBalance(requestDTO.getBalance());
        savedAccount.setEntitledUser(requestDTO.getEntitledUserName());

        when(accountRepository.save(any(BankAccount.class))).thenReturn(savedAccount);

        // Ejecutar el método
        BankAccount result = accountService.createAccount(requestDTO);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(savedAccount.getId(), result.getId());
        assertEquals(requestDTO.getBalance(), result.getBalance());
        assertEquals(requestDTO.getEntitledUserName(), result.getEntitledUser());

        verify(accountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void testGetAccount() {
        // Configurar datos simulados
        when(accountRepository.findById(mockAccount.getId())).thenReturn(Optional.of(mockAccount));

        // Ejecutar el método
        BankAccount result = accountService.getAccount(mockAccount.getId());

        // Verificar resultados
        assertNotNull(result);
        assertEquals(mockAccount.getId(), result.getId());
        assertEquals(mockAccount.getBalance(), result.getBalance());

        verify(accountRepository, times(1)).findById(eq(mockAccount.getId()));
    }

    @Test
    void testGetAccount_NotFound() {
        // Configurar datos simulados
        when(accountRepository.findById(any(String.class))).thenReturn(Optional.empty());

        // Ejecutar el método y verificar excepción
        Exception exception = assertThrows(RuntimeException.class, () -> accountService.getAccount("invalid-id"));

        assertEquals("Account not founded!", exception.getMessage());
        verify(accountRepository, times(1)).findById(eq("invalid-id"));
    }

    @Test
    void testExecuteTransaction() {
        // Configurar datos simulados
        when(accountRepository.findById(mockAccount.getId())).thenReturn(Optional.of(mockAccount));
        when(accountRepository.save(any(BankAccount.class))).thenReturn(mockAccount);
        when(transactionRepository.save(any(TransactionBase.class))).thenReturn(mockTransaction);

        // Ejecutar el método
        BigDecimal updatedBalance = accountService.executeTransaction(mockAccount.getId(), mockTransaction);

        // Verificar resultados
        assertNotNull(updatedBalance);
        assertEquals(BigDecimal.valueOf(800), updatedBalance); // 1000 - 200 (impact)

        verify(accountRepository, times(1)).findById(eq(mockAccount.getId()));
        verify(accountRepository, times(1)).save(any(BankAccount.class));
        verify(transactionRepository, times(1)).save(any(TransactionBase.class));
    }

    @Test
    void testExecuteTransaction_AccountNotFound() {
        // Configurar datos simulados
        when(accountRepository.findById("invalid-account-id")).thenReturn(Optional.empty());

        // Ejecutar el método y verificar excepción
        Exception exception = assertThrows(RuntimeException.class,
                () -> accountService.executeTransaction("invalid-account-id", mockTransaction));

        // Verificar resultados
        assertEquals("Account not founded!", exception.getMessage());
        verify(accountRepository, times(1)).findById(eq("invalid-account-id"));
        verify(transactionRepository, never()).save(any(TransactionBase.class)); // No debe guardar transacción
    }

    @Test
    void testGetTransactionsByAccount() {
        // Configurar datos simulados
        when(transactionRepository.findByAccountId(mockAccount.getId())).thenReturn(List.of(mockTransaction));

        // Ejecutar el método
        List<TransactionBase> transactions = accountService.getTransactionsByAccount(mockAccount.getId());

        // Verificar resultados
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(mockTransaction.getId(), transactions.get(0).getId());

        verify(transactionRepository, times(1)).findByAccountId(eq(mockAccount.getId()));
    }

    @Test
    void testGetTransactionsByAccount_NoTransactions() {
        // Configurar datos simulados
        when(transactionRepository.findByAccountId(mockAccount.getId())).thenReturn(List.of()); // Lista vacía

        // Ejecutar el método
        List<TransactionBase> transactions = accountService.getTransactionsByAccount(mockAccount.getId());

        // Verificar que la lista está vacía
        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());

        verify(transactionRepository, times(1)).findByAccountId(eq(mockAccount.getId()));
    }

    @Test
    void testGetAccountWithTransaction() {
        // Configurar datos simulados
        when(accountRepository.findById(mockAccount.getId())).thenReturn(Optional.of(mockAccount));
        when(transactionRepository.findByAccountId(mockAccount.getId())).thenReturn(List.of(mockTransaction));

        // Ejecutar el método
        AccountResponseDTO result = accountService.getAccountWithTransaction(mockAccount.getId());

        // Verificar resultados
        assertNotNull(result);
        assertEquals(mockAccount.getId(), result.getId());
        assertEquals(mockAccount.getBalance(), result.getBalance());
        assertEquals(1, result.getTransactions().size());
        assertEquals(mockTransaction.getId(), result.getTransactions().get(0).getId());

        verify(accountRepository, times(1)).findById(eq(mockAccount.getId()));
        verify(transactionRepository, times(1)).findByAccountId(eq(mockAccount.getId()));
    }
}
