package claudiaburali.capstoneproject.payloads;

import jakarta.validation.constraints.NotEmpty;

public record NewWalletDTO(
        @NotEmpty(message = "Nome obbligatorio!")
        String name,
        @NotEmpty(message = "Scegli una coppia di valute!")
        String currencyPairName
) {
}
