package ma.ensias.mentorpath.rating.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse d'une évaluation mentor.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RatingResponse {

    private Long id;
    private String studentEmail;
    private String mentorEmail;
    private Integer score;
    private String comment;
    private LocalDateTime createdAt;
}