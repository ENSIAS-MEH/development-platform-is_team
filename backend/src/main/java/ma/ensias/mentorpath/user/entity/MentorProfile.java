package ma.ensias.mentorpath.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Profil complémentaire d'un mentor (alumni ou étudiant avancé).
 * Lié à un User de rôle MENTOR via une relation One-to-One.
 */
@Entity
@Table(name = "mentor_profiles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MentorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Relation vers le compte utilisateur correspondant. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    /** Filière ENSIAS du mentor. */
    @Column(length = 100)
    private String filiere;

    /** Année de promotion (ex: 2022). */
    private Integer promo;

    @Column(columnDefinition = "TEXT")
    private String bio;

    /** Domaines de compétences séparés par des virgules. */
    @Column(columnDefinition = "TEXT")
    private String expertise;

    /** Note moyenne calculée à partir des évaluations (0.0 – 5.0). */
    @Column(precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;

    /** Indique si le mentor accepte de nouvelles demandes de session. */
    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;

    @Column(length = 500)
    private String avatarUrl;
}
