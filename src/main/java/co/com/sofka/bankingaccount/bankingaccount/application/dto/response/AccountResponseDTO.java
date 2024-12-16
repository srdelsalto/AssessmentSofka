package co.com.sofka.bankingaccount.bankingaccount.application.dto.response;

import co.com.sofka.bankingaccount.bankingaccount.application.dto.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AccountResponseDTO {
    private String id;
    private BigDecimal balance;
    private List<TransactionDTO> transactions;
}
