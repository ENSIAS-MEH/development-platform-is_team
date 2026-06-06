package ma.ensias.mentorpath.roadmap.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

/**
 * DTO pour la création et modification d'une roadmap.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RoadmapRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    private String description;
    private String filiere;
    private List<StepRequest> steps;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class StepRequest {
        private String title;
        private String description;
        private Integer stepOrder;
    }
}