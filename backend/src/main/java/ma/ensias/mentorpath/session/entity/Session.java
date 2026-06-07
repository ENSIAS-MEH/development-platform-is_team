package ma.ensias.mentorpath.session.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.ensias.mentorpath.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entité représentant une session de mentorat entre un étudiant et un mentor.
 */
@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Étudiant qui demande la session */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    /** Mentor qui reçoit la demande */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    /** Date et heure souhaitée */
    @Column(nullable = false)
    private LocalDateTime sessionDate;

    /** Message de l'étudiant */
    @Column(columnDefinition = "TEXT")
    private String message;

    /** Statut : PENDING / ACCEPTED / DECLINED */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SessionStatus status = SessionStatus.PENDING;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}