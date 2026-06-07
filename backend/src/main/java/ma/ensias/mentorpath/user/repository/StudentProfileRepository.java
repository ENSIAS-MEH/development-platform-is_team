package ma.ensias.mentorpath.user.repository;

import ma.ensias.mentorpath.user.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository JPA pour les profils étudiants.
 */
@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    /** Récupère le profil étudiant associé à un userId. */
    Optional<StudentProfile> findByUserId(Long userId);
}
