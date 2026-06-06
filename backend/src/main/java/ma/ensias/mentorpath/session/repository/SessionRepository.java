package ma.ensias.mentorpath.session.repository;

import ma.ensias.mentorpath.session.entity.Session;
import ma.ensias.mentorpath.session.entity.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour la gestion des sessions en base de données.
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    /** Trouve toutes les sessions d'un étudiant */
    List<Session> findByStudentId(Long studentId);

    /** Trouve toutes les sessions d'un mentor */
    List<Session> findByMentorId(Long mentorId);

    /** JPQL — Trouve les sessions d'un mentor par statut */
    @Query("SELECT s FROM Session s WHERE s.mentor.id = :mentorId AND s.status = :status")
    List<Session> findByMentorIdAndStatus(
        @Param("mentorId") Long mentorId,
        @Param("status") SessionStatus status
    );

    /** JPQL — Trouve les sessions d'un étudiant par statut */
    @Query("SELECT s FROM Session s WHERE s.student.id = :studentId AND s.status = :status")
    List<Session> findByStudentIdAndStatus(
        @Param("studentId") Long studentId,
        @Param("status") SessionStatus status
    );
}