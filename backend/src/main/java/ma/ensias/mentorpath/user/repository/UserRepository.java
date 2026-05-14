package ma.ensias.mentorpath.user.repository;

import ma.ensias.mentorpath.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository JPA pour l'entité User.
 * Pattern Repository : abstraction complète de la couche de persistance.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** Cherche un utilisateur par son email (utilisé pour l'authentification). */
    Optional<User> findByEmail(String email);

    /** Vérifie si un email est déjà pris (utilisé lors de l'inscription). */
    boolean existsByEmail(String email);
}
