package ma.ensias.mentorpath.message.repository;

import ma.ensias.mentorpath.message.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des conversations.
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /** Trouve toutes les conversations d'un utilisateur */
    @Query("SELECT c FROM Conversation c WHERE c.student.id = :userId OR c.mentor.id = :userId")
    List<Conversation> findByUserId(@Param("userId") Long userId);

    /** Vérifie si une conversation existe déjà */
    Optional<Conversation> findByStudentIdAndMentorId(Long studentId, Long mentorId);
}