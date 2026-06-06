package ma.ensias.mentorpath.session.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO pour la demande d'une session de mentorat.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SessionRequest {

    @NotNull(message = "L'ID du mentor est obligatoire")
    private Long mentorId;

    @NotNull(message = "La date est obligatoire")
    @Future(message = "La date doit être dans le futur")
    private LocalDateTime sessionDate;

    private String message;
}