package ma.ensias.mentorpath.roadmap.repository;

import ma.ensias.mentorpath.roadmap.entity.Roadmap;
import ma.ensias.mentorpath.roadmap.entity.RoadmapEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des roadmaps en base de données.
 */
@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

    /** Trouve toutes les roadmaps d'un mentor */
    List<Roadmap> findByMentorId(Long mentorId);

    /** Trouve toutes les roadmaps par filière */
    List<Roadmap> findByFiliere(String filiere);

    /** JPQL — Recherche par mot clé dans le titre */
    @Query("SELECT r FROM Roadmap r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Roadmap> searchByKeyword(@Param("keyword") String keyword);

    /** JPQL — Recherche par filière et mot clé */
    @Query("SELECT r FROM Roadmap r WHERE r.filiere = :filiere AND LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Roadmap> searchByFiliereAndKeyword(
        @Param("filiere") String filiere,
        @Param("keyword") String keyword
    );
}