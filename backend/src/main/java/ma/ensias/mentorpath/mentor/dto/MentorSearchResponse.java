package ma.ensias.mentorpath.mentor.dto;

import lombok.*;
import java.math.BigDecimal;

/**
 * DTO pour la réponse de recherche de mentors.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MentorSearchResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String filiere;
    private Integer promo;
    private String bio;
    private String expertise;
    private BigDecimal rating;
    private Boolean available;
}