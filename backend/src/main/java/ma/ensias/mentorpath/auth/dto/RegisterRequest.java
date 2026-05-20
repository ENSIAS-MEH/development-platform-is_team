package ma.ensias.mentorpath.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import ma.ensias.mentorpath.user.entity.Role;

/**
 * DTO reçu lors de l'inscription d'un nouvel utilisateur.
 * Les annotations @Valid sur le contrôleur déclenchent la validation automatique.
 */

@Data
public class RegisterRequest {

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100)
    private String lastName;

    /** Filière ENSIAS — obligatoire pour STUDENT et MENTOR. */
    @Size(max = 100)
    private String filiere;

    /** Année d'étude — pour STUDENT uniquement. */
    private Integer anneeEtude;

    /** Année de promotion — pour MENTOR uniquement. */
    private Integer promo;
}
