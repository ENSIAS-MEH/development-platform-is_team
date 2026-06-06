package ma.ensias.mentorpath.session;

import ma.ensias.mentorpath.common.ApiResponse;
import ma.ensias.mentorpath.session.controller.SessionController;
import ma.ensias.mentorpath.session.dto.SessionRequest;
import ma.ensias.mentorpath.session.dto.SessionResponse;
import ma.ensias.mentorpath.session.entity.SessionStatus;
import ma.ensias.mentorpath.session.service.SessionService;
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
@DisplayName("SessionController – Tests unitaires")
class SessionControllerTest {

    @Mock
    private SessionService sessionService;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        when(authentication.getName()).thenReturn("student@ensias.ma");
    }

    @Test
    @DisplayName("requestSession() – doit créer une session PENDING")
    void shouldRequestSession() {
        SessionRequest request = SessionRequest.builder()
                .mentorId(1L)
                .sessionDate(LocalDateTime.now().plusDays(1))
                .message("J'ai besoin d'aide")
                .build();

        SessionResponse response = SessionResponse.builder()
                .id(1L)
                .studentEmail("student@ensias.ma")
                .mentorEmail("mentor@ensias.ma")
                .status(SessionStatus.PENDING)
                .build();

        when(sessionService.requestSession(any(), eq("student@ensias.ma"))).thenReturn(response);

        ResponseEntity<ApiResponse<SessionResponse>> result = sessionController.requestSession(request, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData().getStatus()).isEqualTo(SessionStatus.PENDING);
        verify(sessionService).requestSession(any(), eq("student@ensias.ma"));
    }

    @Test
    @DisplayName("getMySessions() – doit retourner les sessions de l'étudiant")
    void shouldGetMySessions() {
        when(sessionService.getMySessions("student@ensias.ma"))
                .thenReturn(List.of(
                        SessionResponse.builder().id(1L).status(SessionStatus.PENDING).build(),
                        SessionResponse.builder().id(2L).status(SessionStatus.ACCEPTED).build()));

        ResponseEntity<ApiResponse<List<SessionResponse>>> result = sessionController.getMySessions(authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData()).hasSize(2);
        verify(sessionService).getMySessions("student@ensias.ma");
    }

    @Test
    @DisplayName("getMentorSchedule() – doit retourner le planning du mentor")
    void shouldGetMentorSchedule() {
        when(authentication.getName()).thenReturn("mentor@ensias.ma");
        when(sessionService.getMentorSchedule("mentor@ensias.ma"))
                .thenReturn(List.of(
                        SessionResponse.builder().id(1L).status(SessionStatus.PENDING).build()));

        ResponseEntity<ApiResponse<List<SessionResponse>>> result = sessionController.getMentorSchedule(authentication);

        assertThat(result.getBody().getData()).hasSize(1);
        verify(sessionService).getMentorSchedule("mentor@ensias.ma");
    }

    @Test
    @DisplayName("acceptSession() – doit accepter une session")
    void shouldAcceptSession() {
        when(authentication.getName()).thenReturn("mentor@ensias.ma");
        when(sessionService.acceptSession(eq(1L), eq("mentor@ensias.ma")))
                .thenReturn(SessionResponse.builder()
                        .id(1L)
                        .status(SessionStatus.ACCEPTED)
                        .build());

        ResponseEntity<ApiResponse<SessionResponse>> result = sessionController.acceptSession(1L, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData().getStatus()).isEqualTo(SessionStatus.ACCEPTED);
        verify(sessionService).acceptSession(1L, "mentor@ensias.ma");
    }

    @Test
    @DisplayName("declineSession() – doit refuser une session")
    void shouldDeclineSession() {
        when(authentication.getName()).thenReturn("mentor@ensias.ma");
        when(sessionService.declineSession(eq(1L), eq("mentor@ensias.ma")))
                .thenReturn(SessionResponse.builder()
                        .id(1L)
                        .status(SessionStatus.DECLINED)
                        .build());

        ResponseEntity<ApiResponse<SessionResponse>> result = sessionController.declineSession(1L, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData().getStatus()).isEqualTo(SessionStatus.DECLINED);
        verify(sessionService).declineSession(1L, "mentor@ensias.ma");
    }
}
