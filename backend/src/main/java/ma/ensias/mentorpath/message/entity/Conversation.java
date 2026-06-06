package ma.ensias.mentorpath.message.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.ensias.mentorpath.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant une conversation entre un étudiant et un mentor.
 */
@Entity
@Table(name = "conversations",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "mentor_id"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private User mentor;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}