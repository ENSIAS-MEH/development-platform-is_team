package ma.ensias.mentorpath.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO reçu lors de la mise à jour du profil (STUDENT ou MENTOR).
 * Tous les champs sont optionnels : seuls les champs envoyés sont modifiés.
 */
@Data
public class UpdateProfileRequest {

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 100)
    private String filiere;

    @Size(max = 1000)
    private String bio;

    @Size(max = 500)
    private String avatarUrl;

    /** STUDENT uniquement */
    private Integer anneeEtude;

    /** MENTOR uniquement */
    private Integer promo;

    /** MENTOR uniquement */
    private String expertise;

    /** MENTOR uniquement */
    private Boolean available;
}
