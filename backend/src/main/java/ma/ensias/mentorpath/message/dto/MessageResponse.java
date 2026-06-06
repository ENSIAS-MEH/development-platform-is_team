package ma.ensias.mentorpath.message.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO pour la réponse d'un message.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MessageResponse {
    private Long id;
    private String senderEmail;
    private String content;
    private LocalDateTime sentAt;
}