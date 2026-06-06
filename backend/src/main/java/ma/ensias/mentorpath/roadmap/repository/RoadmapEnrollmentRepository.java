package ma.ensias.mentorpath.roadmap.repository;

import ma.ensias.mentorpath.roadmap.entity.RoadmapEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des inscriptions aux roadmaps.
 */
@Repository
public interface RoadmapEnrollmentRepository 
    extends JpaRepository<RoadmapEnrollment, Long> {

    /** Trouve toutes les inscriptions d'un étudiant */
    List<RoadmapEnrollment> findByStudentId(Long studentId);

    /** Trouve toutes les inscriptions à une roadmap */
    List<RoadmapEnrollment> findByRoadmapId(Long roadmapId);

    /** Vérifie si un étudiant est déjà inscrit */
    Optional<RoadmapEnrollment> findByStudentIdAndRoadmapId(
        Long studentId, 
        Long roadmapId
    );
}