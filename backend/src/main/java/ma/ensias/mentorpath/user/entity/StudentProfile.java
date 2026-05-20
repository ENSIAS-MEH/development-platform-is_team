package ma.ensias.mentorpath.user.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Profil complémentaire d'un étudiant.
 * Lié à un User de rôle STUDENT via une relation One-to-One.
 */
@Entity
@Table(name = "student_profiles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class StudentProfile {

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

    /** Filière ENSIAS (ex: Génie Logiciel, Génie des Données…). */
    @Column(length = 100)
    private String filiere;

    /** Année d'étude. */
    private Integer anneeEtude;

    /** Présentation courte de l'étudiant. */
    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 500)
    private String avatarUrl;
}
