package ma.ensias.mentorpath.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO retourné après une authentification réussie (inscription ou connexion).
 * Le frontend stocke le token JWT et l'envoie dans le header Authorization.
 */
@Data
@AllArgsConstructor
public class JwtResponse {

    /** Token JWT à inclure dans : Authorization: Bearer {token} */
    private String token;

    private String email;

    /** Rôle de l'utilisateur : STUDENT, MENTOR ou ADMIN */
    private String role;
}
