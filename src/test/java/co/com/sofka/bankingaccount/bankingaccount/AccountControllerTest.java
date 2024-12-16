package co.com.sofka.bankingaccount.bankingaccount;

import co.com.sofka.bankingaccount.bankingaccount.application.dto.request.CreateAccountRequestDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.request.TransactionRequestDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.response.AccountResponseDTO;
import co.com.sofka.bankingaccount.bankingaccount.application.dto.response.AccountResponseTransactionDTO;
import co.com.sofka.bankingaccount.bankingaccount.controllers.AccountController;
import co.com.sofka.bankingaccount.bankingaccount.factory.TransactionFactory;
import co.com.sofka.bankingaccount.bankingaccount.model.BankAccount;
import co.com.sofka.bankingaccount.bankingaccount.model.transaction.TransactionBase;
import co.com.sofka.bankingaccount.bankingaccount.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AccountControllerTest {
    private AccountService accountService;
    private TransactionFactory transactionFactory;
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        accountService = Mockito.mock(AccountService.class);
        transactionFactory = Mockito.mock(TransactionFactory.class);
        accountController = new AccountController(accountService, transactionFactory);
    }

    // Prueba positiva para crear una cuenta bancaria
    @Test
    void testCreateBankAccount_Success() {
        // Simular datos de entrada y salida
        CreateAccountRequestDTO createRequest = new CreateAccountRequestDTO("John Doe", BigDecimal.valueOf(1000));
        BankAccount mockAccount = new BankAccount("1", "John Doe", BigDecimal.valueOf(1000));

        // Mock de servicio
        when(accountService.createAccount(createRequest)).thenReturn(mockAccount);

        // Ejecutar el método
        BankAccount result = accountController.createBankAccount(createRequest);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(mockAccount.getId(), result.getId());
        assertEquals(mockAccount.getEntitledUser(), result.getEntitledUser());
        assertEquals(mockAccount.getBalance(), result.getBalance());
        verify(accountService, times(1)).createAccount(createRequest);
    }

    // Prueba negativa para crear una cuenta bancaria con datos inválidos
    @Test
    void testCreateBankAccount_InvalidRequest() {
        // Datos de entrada inválidos
        CreateAccountRequestDTO invalidRequest = new CreateAccountRequestDTO(null, BigDecimal.valueOf(1000));

        // Esperar excepción
        assertThrows(Exception.class, () -> accountController.createBankAccount(invalidRequest));

        // Verificar que el servicio no se llama
        verify(accountService, never()).createAccount(any());
    }

    // Prueba positiva para ejecutar una transacción
    @Test
    void testExecuteTransaction_Success() {
        // Simular datos de entrada y salida
        TransactionRequestDTO request = new TransactionRequestDTO("1", "DEPOSIT", BigDecimal.valueOf(500));
        BankAccount mockAccount = new BankAccount("1", "John Doe", BigDecimal.valueOf(1000));
        TransactionBase mockTransaction = mock(TransactionBase.class);
        BigDecimal updatedBalance = BigDecimal.valueOf(1500);

        // Mock del servicio y la fábrica
        when(accountService.getAccount(request.getId())).thenReturn(mockAccount);
        when(transactionFactory.createTransaction(request.getType(), request.getAmount(), request.getId()))
                .thenReturn(mockTransaction);
        when(accountService.executeTransaction(mockAccount.getId(), mockTransaction)).thenReturn(updatedBalance);

        // Ejecutar el método
        ResponseEntity<AccountResponseTransactionDTO> response = accountController.executeTransaction(request);

        // Verificar resultados
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockAccount.getId(), response.getBody().getId());
        assertEquals(updatedBalance, response.getBody().getBalance());
        verify(accountService, times(1)).getAccount(request.getId());
        verify(transactionFactory, times(1)).createTransaction(request.getType(), request.getAmount(), request.getId());
        verify(accountService, times(1)).executeTransaction(mockAccount.getId(), mockTransaction);
    }

    // Prueba negativa para ejecutar una transacción con cuenta no existente
    @Test
    void testExecuteTransaction_AccountNotFound() {
        // Simular datos de entrada
        TransactionRequestDTO request = new TransactionRequestDTO("invalid-id", "DEPOSIT", BigDecimal.valueOf(500));

        // Mock del servicio
        when(accountService.getAccount(request.getId())).thenThrow(new RuntimeException("Account not founded!"));

        // Ejecutar el método y verificar excepción
        Exception exception = assertThrows(RuntimeException.class, () -> accountController.executeTransaction(request));
        assertEquals("Account not founded!", exception.getMessage());
        verify(accountService, times(1)).getAccount(request.getId());
        verify(transactionFactory, never()).createTransaction(any(), any(), any());
        verify(accountService, never()).executeTransaction(any(), any());
    }

    // Prueba positiva para obtener una cuenta con transacciones
    @Test
    void testGetAccountWithTransactions_Success() {
        // Simular datos de entrada y salida
        String accountId = "1";
        AccountResponseDTO mockResponse = new AccountResponseDTO(accountId, BigDecimal.valueOf(1000), List.of());

        // Mock del servicio
        when(accountService.getAccountWithTransaction(accountId)).thenReturn(mockResponse);

        // Ejecutar el método
        ResponseEntity<AccountResponseDTO> response = accountController.getAccountWithTransactions(accountId);

        // Verificar resultados
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockResponse.getId(), response.getBody().getId());
        assertEquals(mockResponse.getBalance(), response.getBody().getBalance());
        assertTrue(response.getBody().getTransactions().isEmpty());
        verify(accountService, times(1)).getAccountWithTransaction(accountId);
    }

    // Prueba negativa para obtener una cuenta inexistente
    @Test
    void testGetAccountWithTransactions_AccountNotFound() {
        // Simular datos de entrada
        String invalidAccountId = "invalid-id";

        // Mock del servicio
        when(accountService.getAccountWithTransaction(invalidAccountId))
                .thenThrow(new RuntimeException("Account not founded!"));

        // Ejecutar el método y verificar excepción
        Exception exception = assertThrows(RuntimeException.class,
                () -> accountController.getAccountWithTransactions(invalidAccountId));
        assertEquals("Account not founded!", exception.getMessage());
        verify(accountService, times(1)).getAccountWithTransaction(invalidAccountId);
    }
}
