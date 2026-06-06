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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SessionController – Tests unitaires")
class SessionControllerTest {

    @Mock private SessionService sessionService;
    @Mock private Authentication authentication;
    @InjectMocks private SessionController sessionController;

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
            .status(SessionStatus.PENDING)
            .build();

        when(sessionService.requestSession(any(), anyString())).thenReturn(response);

        ResponseEntity<ApiResponse<SessionResponse>> result =
            sessionController.requestSession(request, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData().getStatus()).isEqualTo(SessionStatus.PENDING);
    }

    @Test
    @DisplayName("acceptSession() – doit accepter une session")
    void shouldAcceptSession() {
        when(authentication.getName()).thenReturn("mentor@ensias.ma");
        when(sessionService.acceptSession(any(), anyString()))
            .thenReturn(SessionResponse.builder()
                .id(1L)
                .status(SessionStatus.ACCEPTED)
                .build());

        ResponseEntity<ApiResponse<SessionResponse>> result =
            sessionController.acceptSession(1L, authentication);

        assertThat(result.getBody().getData().getStatus()).isEqualTo(SessionStatus.ACCEPTED);
    }

    @Test
    @DisplayName("getMentorSchedule() – doit retourner le planning du mentor")
    void shouldGetMentorSchedule() {
        when(authentication.getName()).thenReturn("mentor@ensias.ma");
        when(sessionService.getMentorSchedule(anyString()))
            .thenReturn(List.of(
                SessionResponse.builder().id(1L).status(SessionStatus.PENDING).build()
            ));

        ResponseEntity<ApiResponse<List<SessionResponse>>> result =
            sessionController.getMentorSchedule(authentication);

        assertThat(result.getBody().getData()).hasSize(1);
    }
}