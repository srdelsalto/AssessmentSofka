package co.com.sofka.bankingaccount.bankingaccount.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDTO {
    private String id;
    private String type;
    private BigDecimal amount;
    private BigDecimal cost;
}
