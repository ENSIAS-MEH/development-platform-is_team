package ma.ensias.mentorpath.mentor.repository;

import ma.ensias.mentorpath.user.entity.MentorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository pour la recherche avancée de mentors.
 */
@Repository
public interface MentorSearchRepository extends JpaRepository<MentorProfile, Long> {

    /** JPQL — Recherche avancée avec filtres */
    @Query("SELECT m FROM MentorProfile m WHERE " +
           "(:filiere IS NULL OR m.filiere = :filiere) AND " +
           "(:minRating IS NULL OR m.rating >= :minRating) AND " +
           "(:available IS NULL OR m.available = :available)")
    List<MentorProfile> searchMentors(
        @Param("filiere") String filiere,
        @Param("minRating") BigDecimal minRating,
        @Param("available") Boolean available
    );
}