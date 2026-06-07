package ma.ensias.mentorpath.roadmap.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant une étape dans une roadmap.
 */
@Entity
@Table(name = "roadmap_steps")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RoadmapStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    /** Ordre de l'étape dans la roadmap (1, 2, 3...) */
    @Column(nullable = false)
    private Integer stepOrder;

    /** Roadmap à laquelle appartient cette étape */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    private Roadmap roadmap;
}