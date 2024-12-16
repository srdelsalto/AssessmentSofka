package co.com.sofka.bankingaccount.bankingaccount.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
    @NotNull(message = "The account id cant be empty")
    @NotBlank
    private String id;

    @NotBlank(message = "The transaction type cant be empty")
    private String type;

    @Min(value = 0, message = "The amount should be equal or higher than 0")
    private BigDecimal amount;
}
