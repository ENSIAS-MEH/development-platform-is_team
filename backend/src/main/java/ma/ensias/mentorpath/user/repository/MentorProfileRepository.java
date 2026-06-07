package ma.ensias.mentorpath.user.repository;

import ma.ensias.mentorpath.user.entity.MentorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository JPA pour les profils mentors.
 */
@Repository
public interface MentorProfileRepository extends JpaRepository<MentorProfile, Long> {

    /** Récupère le profil mentor associé à un userId. */
    Optional<MentorProfile> findByUserId(Long userId);
}
