package claudiaburali.capstoneproject.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserLoginDTO(
        @Email(message = "Indirizzo email non valido!")
        String email,
        @NotEmpty(message = "Campo obbligatorio!")
        String password) {
}
