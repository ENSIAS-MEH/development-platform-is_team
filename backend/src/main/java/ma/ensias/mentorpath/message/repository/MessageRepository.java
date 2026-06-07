package ma.ensias.mentorpath.message.repository;

import ma.ensias.mentorpath.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour la gestion des messages.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /** Trouve tous les messages d'une conversation triés par date */
    List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);
}