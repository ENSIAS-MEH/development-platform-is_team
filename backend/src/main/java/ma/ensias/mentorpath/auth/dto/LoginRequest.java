package ma.ensias.mentorpath.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO reçu lors de la connexion d'un utilisateur.
 */
@Data
public class LoginRequest {

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}
