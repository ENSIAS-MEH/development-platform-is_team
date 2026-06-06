package ma.ensias.mentorpath.rating.repository;

import ma.ensias.mentorpath.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des évaluations en base de données.
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByMentorId(Long mentorId);

    Optional<Rating> findByStudentIdAndMentorId(Long studentId, Long mentorId);

    /** JPQL — Calcule la moyenne des notes d'un mentor */
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.mentor.id = :mentorId")
    Double calculateAverageRating(@Param("mentorId") Long mentorId);
}