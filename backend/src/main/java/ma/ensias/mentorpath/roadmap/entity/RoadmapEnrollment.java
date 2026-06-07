package ma.ensias.mentorpath.roadmap.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.ensias.mentorpath.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entité représentant l'inscription d'un étudiant à une roadmap.
 * Permet de suivre la progression de chaque étudiant.
 */
@Entity
@Table(
    name = "roadmap_enrollments",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"student_id", "roadmap_id"}
    )
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RoadmapEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Étudiant inscrit à la roadmap */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    /** Roadmap suivie */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id", nullable = false)
    private Roadmap roadmap;

    /** Progression en pourcentage (0 à 100) */
    @Column(nullable = false)
    @Builder.Default
    private Integer progressPercent = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime enrolledAt;
}