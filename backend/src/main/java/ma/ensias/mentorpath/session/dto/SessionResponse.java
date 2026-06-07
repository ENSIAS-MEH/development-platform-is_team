package ma.ensias.mentorpath.session.dto;

import lombok.*;
import ma.ensias.mentorpath.session.entity.SessionStatus;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse d'une session de mentorat.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SessionResponse {

    private Long id;
    private String studentEmail;
    private String studentName;
    private String mentorEmail;
    private String mentorName;
    private LocalDateTime sessionDate;
    private String message;
    private SessionStatus status;
    private LocalDateTime createdAt;
}