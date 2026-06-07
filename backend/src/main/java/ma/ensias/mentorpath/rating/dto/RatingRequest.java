package ma.ensias.mentorpath.rating.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO pour la création d'une évaluation mentor.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RatingRequest {

    @NotNull(message = "L'ID du mentor est obligatoire")
    private Long mentorId;

    @NotNull(message = "La note est obligatoire")
    @Min(value = 1, message = "La note minimum est 1")
    @Max(value = 5, message = "La note maximum est 5")
    private Integer score;

    private String comment;
}