package ma.ensias.mentorpath.user.dto;

import lombok.Data;

/**
 * DTO de réponse représentant un utilisateur.
 * Ne contient jamais le mot de passe.
 */
@Data
public class UserDTO {
    private Long   id;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private String filiere;
    private String bio;
    private String avatarUrl;
}
