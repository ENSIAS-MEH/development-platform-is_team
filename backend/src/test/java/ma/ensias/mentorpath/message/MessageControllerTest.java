package ma.ensias.mentorpath.message;

import ma.ensias.mentorpath.common.ApiResponse;
import ma.ensias.mentorpath.message.controller.MessageController;
import ma.ensias.mentorpath.message.dto.ConversationResponse;
import ma.ensias.mentorpath.message.dto.MessageRequest;
import ma.ensias.mentorpath.message.dto.MessageResponse;
import ma.ensias.mentorpath.message.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("MessageController - Tests unitaires")
class MessageControllerTest {

    @Mock private MessageService messageService;
    @Mock private Authentication authentication;
    @InjectMocks private MessageController messageController;

    @BeforeEach
    void setUp() {
        when(authentication.getName()).thenReturn("student@ensias.ma");
    }

    @Test
    @DisplayName("createConversation() - doit creer une conversation")
    void shouldCreateConversation() {
        ConversationResponse response = ConversationResponse.builder()
            .id(1L)
            .studentEmail("student@ensias.ma")
            .mentorEmail("mentor@ensias.ma")
            .messages(List.of())
            .build();

        when(messageService.createConversation(eq(2L), eq("student@ensias.ma")))
            .thenReturn(response);

        ResponseEntity<ApiResponse<ConversationResponse>> result =
            messageController.createConversation(2L, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData().getStudentEmail()).isEqualTo("student@ensias.ma");
        verify(messageService).createConversation(2L, "student@ensias.ma");
    }

    @Test
    @DisplayName("getMyConversations() - doit retourner les conversations")
    void shouldGetMyConversations() {
        when(messageService.getMyConversations("student@ensias.ma"))
            .thenReturn(List.of(
                ConversationResponse.builder().id(1L).build(),
                ConversationResponse.builder().id(2L).build()
            ));

        ResponseEntity<ApiResponse<List<ConversationResponse>>> result =
            messageController.getMyConversations(authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData()).hasSize(2);
        verify(messageService).getMyConversations("student@ensias.ma");
    }

    @Test
    @DisplayName("getMessages() - doit retourner les messages d'une conversation")
    void shouldGetMessages() {
        when(messageService.getMessages(eq(1L), eq("student@ensias.ma")))
            .thenReturn(List.of(
                MessageResponse.builder().id(1L).content("Bonjour").build(),
                MessageResponse.builder().id(2L).content("Comment ca va?").build()
            ));

        ResponseEntity<ApiResponse<List<MessageResponse>>> result =
            messageController.getMessages(1L, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData()).hasSize(2);
        verify(messageService).getMessages(1L, "student@ensias.ma");
    }

    @Test
    @DisplayName("sendMessage() - doit envoyer un message")
    void shouldSendMessage() {
        MessageRequest request = MessageRequest.builder().content("Merci!").build();

        MessageResponse response = MessageResponse.builder()
            .id(1L)
            .senderEmail("student@ensias.ma")
            .content("Merci!")
            .sentAt(LocalDateTime.now())
            .build();

        when(messageService.sendMessage(eq(1L), any(), eq("student@ensias.ma")))
            .thenReturn(response);

        ResponseEntity<ApiResponse<MessageResponse>> result =
            messageController.sendMessage(1L, request, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData().getContent()).isEqualTo("Merci!");
        verify(messageService).sendMessage(eq(1L), any(), eq("student@ensias.ma"));
    }
}
