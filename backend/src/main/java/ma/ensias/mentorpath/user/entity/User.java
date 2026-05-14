package ma.ensias.mentorpath.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entité principale représentant un utilisateur de la plateforme.
 * Contient les informations d'authentification et le rôle.
 */
@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Adresse email — identifiant unique de connexion. */
    @Email
    @NotBlank
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /** Mot de passe hashé en BCrypt — jamais retourné en réponse API. */
    @NotBlank
    @Column(nullable = false)
    private String password;

    /** Rôle de l'utilisateur : STUDENT, MENTOR ou ADMIN. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /** Compte actif ou désactivé par un admin. */
    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
