package claudiaburali.capstoneproject.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserPasswordDTO(
        @NotEmpty(message = "Dato obbligatorio!")
        @Size(min = 4, message = "La password deve avere almeno 4 caratteri!")
        String password) {
}
