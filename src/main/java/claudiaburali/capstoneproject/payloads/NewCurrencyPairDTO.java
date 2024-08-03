package claudiaburali.capstoneproject.payloads;

import jakarta.validation.constraints.NotEmpty;

public record NewCurrencyPairDTO(
        @NotEmpty(message = "Nome coppia obbligatorio!")
        String name,
        @NotEmpty(message = "Valuta base obbligatoria!")
        String baseCurrency,
        @NotEmpty(message = "Valuta acquisto obbligatoria!")
        String quoteCurrency,
        @NotEmpty(message = "Ticker valuta base obbligatorio!")
        String baseTicker,
        @NotEmpty(message = "Ticker valuta acquisto obbligatorio!")
        String quoteTicker
) {
}
