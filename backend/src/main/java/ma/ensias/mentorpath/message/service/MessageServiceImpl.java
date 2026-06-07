package ma.ensias.mentorpath.message.service;

import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.exception.ResourceNotFoundException;
import ma.ensias.mentorpath.message.dto.ConversationResponse;
import ma.ensias.mentorpath.message.dto.MessageRequest;
import ma.ensias.mentorpath.message.dto.MessageResponse;
import ma.ensias.mentorpath.message.entity.Conversation;
import ma.ensias.mentorpath.message.entity.Message;
import ma.ensias.mentorpath.message.repository.ConversationRepository;
import ma.ensias.mentorpath.message.repository.MessageRepository;
import ma.ensias.mentorpath.user.entity.User;
import ma.ensias.mentorpath.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de messagerie interne.
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public ConversationResponse createConversation(Long otherUserId, String userEmail) {
        User currentUser = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        User otherUser = userRepository.findById(otherUserId)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        // Vérifie si conversation existe déjà
        return conversationRepository
            .findByStudentIdAndMentorId(currentUser.getId(), otherUser.getId())
            .map(this::toConversationResponse)
            .orElseGet(() -> {
                Conversation conversation = Conversation.builder()
                    .student(currentUser)
                    .mentor(otherUser)
                    .build();
                return toConversationResponse(
                    conversationRepository.save(conversation));
            });
    }

    @Override
    public List<ConversationResponse> getMyConversations(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        return conversationRepository.findByUserId(user.getId())
            .stream()
            .map(this::toConversationResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponse> getMessages(Long conversationId, String userEmail) {
        return messageRepository
            .findByConversationIdOrderBySentAtAsc(conversationId)
            .stream()
            .map(this::toMessageResponse)
            .collect(Collectors.toList());
    }

    @Override
    public MessageResponse sendMessage(Long conversationId, MessageRequest request, String userEmail) {
        User sender = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new ResourceNotFoundException("Conversation non trouvée"));

        Message message = Message.builder()
            .conversation(conversation)
            .sender(sender)
            .content(request.getContent())
            .build();

        return toMessageResponse(messageRepository.save(message));
    }

    private ConversationResponse toConversationResponse(Conversation c) {
        List<MessageResponse> messages = messageRepository
            .findByConversationIdOrderBySentAtAsc(c.getId())
            .stream()
            .map(this::toMessageResponse)
            .collect(Collectors.toList());

        return ConversationResponse.builder()
            .id(c.getId())
            .studentEmail(c.getStudent().getEmail())
            .mentorEmail(c.getMentor().getEmail())
            .messages(messages)
            .createdAt(c.getCreatedAt())
            .build();
    }

    private MessageResponse toMessageResponse(Message m) {
        return MessageResponse.builder()
            .id(m.getId())
            .senderEmail(m.getSender().getEmail())
            .content(m.getContent())
            .sentAt(m.getSentAt())
            .build();
    }
}