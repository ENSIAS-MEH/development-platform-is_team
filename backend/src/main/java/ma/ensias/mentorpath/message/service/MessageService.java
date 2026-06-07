package ma.ensias.mentorpath.message.service;

import ma.ensias.mentorpath.message.dto.ConversationResponse;
import ma.ensias.mentorpath.message.dto.MessageRequest;
import ma.ensias.mentorpath.message.dto.MessageResponse;

import java.util.List;

/**
 * Service interface pour la gestion de la messagerie interne.
 */
public interface MessageService {
    ConversationResponse createConversation(Long otherUserId, String userEmail);
    List<ConversationResponse> getMyConversations(String userEmail);
    List<MessageResponse> getMessages(Long conversationId, String userEmail);
    MessageResponse sendMessage(Long conversationId, MessageRequest request, String userEmail);
}