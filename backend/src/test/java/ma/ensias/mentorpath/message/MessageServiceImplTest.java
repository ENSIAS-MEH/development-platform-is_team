package ma.ensias.mentorpath.message;

import ma.ensias.mentorpath.exception.ResourceNotFoundException;
import ma.ensias.mentorpath.message.dto.ConversationResponse;
import ma.ensias.mentorpath.message.dto.MessageRequest;
import ma.ensias.mentorpath.message.dto.MessageResponse;
import ma.ensias.mentorpath.message.entity.Conversation;
import ma.ensias.mentorpath.message.entity.Message;
import ma.ensias.mentorpath.message.repository.ConversationRepository;
import ma.ensias.mentorpath.message.repository.MessageRepository;
import ma.ensias.mentorpath.message.service.MessageServiceImpl;
import ma.ensias.mentorpath.user.entity.User;
import ma.ensias.mentorpath.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MessageServiceImpl - Tests unitaires")
class MessageServiceImplTest {

    @Mock private ConversationRepository conversationRepository;
    @Mock private MessageRepository messageRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private MessageServiceImpl messageService;

    private User student;
    private User mentor;
    private Conversation conversation;
    private Message message;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setEmail("student@ensias.ma");

        mentor = new User();
        mentor.setId(2L);
        mentor.setEmail("mentor@ensias.ma");

        conversation = Conversation.builder()
            .id(1L).student(student).mentor(mentor).build();

        message = Message.builder()
            .id(1L).conversation(conversation).sender(student).content("Bonjour").build();
    }

    @Test
    @DisplayName("createConversation() - cree une nouvelle conversation")
    void shouldCreateNewConversation() {
        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mentor));
        when(conversationRepository.findByStudentIdAndMentorId(1L, 2L))
            .thenReturn(Optional.empty());
        when(conversationRepository.save(any())).thenReturn(conversation);
        when(messageRepository.findByConversationIdOrderBySentAtAsc(1L)).thenReturn(List.of());

        ConversationResponse result = messageService.createConversation(2L, "student@ensias.ma");

        assertThat(result.getStudentEmail()).isEqualTo("student@ensias.ma");
        assertThat(result.getMentorEmail()).isEqualTo("mentor@ensias.ma");
        verify(conversationRepository).save(any());
    }

    @Test
    @DisplayName("createConversation() - retourne conversation existante")
    void shouldReturnExistingConversation() {
        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mentor));
        when(conversationRepository.findByStudentIdAndMentorId(1L, 2L))
            .thenReturn(Optional.of(conversation));
        when(messageRepository.findByConversationIdOrderBySentAtAsc(1L)).thenReturn(List.of());

        ConversationResponse result = messageService.createConversation(2L, "student@ensias.ma");

        assertThat(result.getId()).isEqualTo(1L);
        verify(conversationRepository, never()).save(any());
    }

    @Test
    @DisplayName("createConversation() - utilisateur introuvable -> ResourceNotFoundException")
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("inconnu@ensias.ma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.createConversation(2L, "inconnu@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getMyConversations() - doit retourner les conversations")
    void shouldGetMyConversations() {
        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(conversationRepository.findByUserId(1L)).thenReturn(List.of(conversation));
        when(messageRepository.findByConversationIdOrderBySentAtAsc(1L)).thenReturn(List.of());

        List<ConversationResponse> result = messageService.getMyConversations("student@ensias.ma");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStudentEmail()).isEqualTo("student@ensias.ma");
    }

    @Test
    @DisplayName("getMyConversations() - utilisateur introuvable -> ResourceNotFoundException")
    void shouldThrowWhenUserNotFoundOnGetConversations() {
        when(userRepository.findByEmail("inconnu@ensias.ma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.getMyConversations("inconnu@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getMessages() - doit retourner les messages d'une conversation")
    void shouldGetMessages() {
        when(messageRepository.findByConversationIdOrderBySentAtAsc(1L))
            .thenReturn(List.of(message));

        List<MessageResponse> result = messageService.getMessages(1L, "student@ensias.ma");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("Bonjour");
        assertThat(result.get(0).getSenderEmail()).isEqualTo("student@ensias.ma");
    }

    @Test
    @DisplayName("getMessages() - liste vide si pas de messages")
    void shouldReturnEmptyWhenNoMessages() {
        when(messageRepository.findByConversationIdOrderBySentAtAsc(99L)).thenReturn(List.of());

        List<MessageResponse> result = messageService.getMessages(99L, "student@ensias.ma");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("sendMessage() - cas nominal")
    void shouldSendMessage() {
        MessageRequest request = MessageRequest.builder().content("Salut!").build();

        Message savedMessage = Message.builder()
            .id(2L).conversation(conversation).sender(student).content("Salut!").build();

        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(conversationRepository.findById(1L)).thenReturn(Optional.of(conversation));
        when(messageRepository.save(any())).thenReturn(savedMessage);

        MessageResponse result = messageService.sendMessage(1L, request, "student@ensias.ma");

        assertThat(result.getContent()).isEqualTo("Salut!");
        assertThat(result.getSenderEmail()).isEqualTo("student@ensias.ma");
        verify(messageRepository).save(any());
    }

    @Test
    @DisplayName("sendMessage() - sender introuvable -> ResourceNotFoundException")
    void shouldThrowWhenSenderNotFound() {
        when(userRepository.findByEmail("inconnu@ensias.ma")).thenReturn(Optional.empty());
        MessageRequest request = MessageRequest.builder().content("Test").build();

        assertThatThrownBy(() -> messageService.sendMessage(1L, request, "inconnu@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("sendMessage() - conversation introuvable -> ResourceNotFoundException")
    void shouldThrowWhenConversationNotFound() {
        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(conversationRepository.findById(99L)).thenReturn(Optional.empty());
        MessageRequest request = MessageRequest.builder().content("Test").build();

        assertThatThrownBy(() -> messageService.sendMessage(99L, request, "student@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}
