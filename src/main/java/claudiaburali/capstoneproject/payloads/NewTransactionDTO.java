package claudiaburali.capstoneproject.payloads;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewTransactionDTO(
        @NotNull(message = "Il volume è un campo obbligatorio!")
        double volume,
        @NotNull(message = "Il value è un campo obbligatorio!")
        double value,
        @NotNull(message = "L'amount è un campo obbligatorio!")
        double amount,
        LocalDate date,
        String exchange
) {
}
