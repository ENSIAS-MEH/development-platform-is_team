package ma.ensias.mentorpath.message.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO pour l'envoi d'un message.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MessageRequest {

    @NotBlank(message = "Le contenu est obligatoire")
    private String content;
}