package co.com.sofka.bankingaccount.bankingaccount.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AccountResponseTransactionDTO {
    private String id;
    private BigDecimal balance;
}
