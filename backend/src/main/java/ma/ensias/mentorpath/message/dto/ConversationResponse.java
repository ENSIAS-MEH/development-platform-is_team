package ma.ensias.mentorpath.message.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour la réponse d'une conversation.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ConversationResponse {
    private Long id;
    private String studentEmail;
    private String mentorEmail;
    private List<MessageResponse> messages;
    private LocalDateTime createdAt;
}