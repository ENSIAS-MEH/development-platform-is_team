package ma.ensias.mentorpath.roadmap.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour la réponse d'une roadmap.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RoadmapResponse {

    private Long id;
    private String title;
    private String description;
    private String filiere;
    private String mentorName;
    private Integer enrollmentCount;
    private Integer progressPercent;
    private List<StepResponse> steps;
    private LocalDateTime createdAt;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class StepResponse {
        private Long id;
        private String title;
        private String description;
        private Integer stepOrder;
    }
}