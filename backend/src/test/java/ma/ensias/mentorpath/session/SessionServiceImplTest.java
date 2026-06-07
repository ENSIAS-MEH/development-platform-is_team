package ma.ensias.mentorpath.session;

import ma.ensias.mentorpath.exception.ResourceNotFoundException;
import ma.ensias.mentorpath.session.dto.SessionRequest;
import ma.ensias.mentorpath.session.dto.SessionResponse;
import ma.ensias.mentorpath.session.entity.Session;
import ma.ensias.mentorpath.session.entity.SessionStatus;
import ma.ensias.mentorpath.session.repository.SessionRepository;
import ma.ensias.mentorpath.session.service.SessionServiceImpl;
import ma.ensias.mentorpath.user.entity.User;
import ma.ensias.mentorpath.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SessionServiceImpl – Tests unitaires")
class SessionServiceImplTest {

    @Mock private SessionRepository sessionRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private SessionServiceImpl sessionService;

    private User student;
    private User mentor;
    private Session session;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setEmail("student@ensias.ma");

        mentor = new User();
        mentor.setId(2L);
        mentor.setEmail("mentor@ensias.ma");

        session = Session.builder()
            .id(1L)
            .student(student)
            .mentor(mentor)
            .sessionDate(LocalDateTime.now().plusDays(1))
            .message("Aide svp")
            .status(SessionStatus.PENDING)
            .build();
    }

    // ──────────────────────────────────────────────────────────────
    // requestSession
    // ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("requestSession() – cas nominal")
    void shouldRequestSession() {
        SessionRequest request = SessionRequest.builder()
            .mentorId(2L)
            .sessionDate(LocalDateTime.now().plusDays(1))
            .message("Aide svp")
            .build();

        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mentor));
        when(sessionRepository.save(any())).thenReturn(session);

        SessionResponse result = sessionService.requestSession(request, "student@ensias.ma");

        assertThat(result.getStatus()).isEqualTo(SessionStatus.PENDING);
        assertThat(result.getStudentEmail()).isEqualTo("student@ensias.ma");
        verify(sessionRepository).save(any());
    }

    @Test
    @DisplayName("requestSession() – étudiant introuvable → ResourceNotFoundException")
    void shouldThrowWhenStudentNotFound() {
        when(userRepository.findByEmail("inconnu@ensias.ma")).thenReturn(Optional.empty());
        SessionRequest request = SessionRequest.builder().mentorId(2L)
            .sessionDate(LocalDateTime.now().plusDays(1)).build();

        assertThatThrownBy(() -> sessionService.requestSession(request, "inconnu@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("requestSession() – mentor introuvable → ResourceNotFoundException")
    void shouldThrowWhenMentorNotFound() {
        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        SessionRequest request = SessionRequest.builder().mentorId(99L)
            .sessionDate(LocalDateTime.now().plusDays(1)).build();

        assertThatThrownBy(() -> sessionService.requestSession(request, "student@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ──────────────────────────────────────────────────────────────
    // getMySessions
    // ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getMySessions() – doit retourner les sessions de l'étudiant")
    void shouldGetMySessions() {
        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(sessionRepository.findByStudentId(1L)).thenReturn(List.of(session));

        List<SessionResponse> result = sessionService.getMySessions("student@ensias.ma");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMentorEmail()).isEqualTo("mentor@ensias.ma");
    }

    @Test
    @DisplayName("getMySessions() – étudiant introuvable → ResourceNotFoundException")
    void shouldThrowWhenStudentNotFoundOnGetMySessions() {
        when(userRepository.findByEmail("inconnu@ensias.ma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.getMySessions("inconnu@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ──────────────────────────────────────────────────────────────
    // getMentorSchedule
    // ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getMentorSchedule() – doit retourner le planning du mentor")
    void shouldGetMentorSchedule() {
        when(userRepository.findByEmail("mentor@ensias.ma")).thenReturn(Optional.of(mentor));
        when(sessionRepository.findByMentorId(2L)).thenReturn(List.of(session));

        List<SessionResponse> result = sessionService.getMentorSchedule("mentor@ensias.ma");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStudentEmail()).isEqualTo("student@ensias.ma");
    }

    @Test
    @DisplayName("getMentorSchedule() – mentor introuvable → ResourceNotFoundException")
    void shouldThrowWhenMentorNotFoundOnGetSchedule() {
        when(userRepository.findByEmail("inconnu@ensias.ma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.getMentorSchedule("inconnu@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ──────────────────────────────────────────────────────────────
    // acceptSession
    // ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("acceptSession() – cas nominal")
    void shouldAcceptSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SessionResponse result = sessionService.acceptSession(1L, "mentor@ensias.ma");

        assertThat(result.getStatus()).isEqualTo(SessionStatus.ACCEPTED);
        verify(sessionRepository).save(session);
    }

    @Test
    @DisplayName("acceptSession() – session introuvable → ResourceNotFoundException")
    void shouldThrowWhenSessionNotFoundOnAccept() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.acceptSession(99L, "mentor@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("acceptSession() – mauvais mentor → RuntimeException")
    void shouldThrowWhenWrongMentorOnAccept() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> sessionService.acceptSession(1L, "autre@ensias.ma"))
            .isInstanceOf(RuntimeException.class);
    }

    // ──────────────────────────────────────────────────────────────
    // declineSession
    // ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("declineSession() – cas nominal")
    void shouldDeclineSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SessionResponse result = sessionService.declineSession(1L, "mentor@ensias.ma");

        assertThat(result.getStatus()).isEqualTo(SessionStatus.DECLINED);
        verify(sessionRepository).save(session);
    }

    @Test
    @DisplayName("declineSession() – session introuvable → ResourceNotFoundException")
    void shouldThrowWhenSessionNotFoundOnDecline() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sessionService.declineSession(99L, "mentor@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("declineSession() – mauvais mentor → RuntimeException")
    void shouldThrowWhenWrongMentorOnDecline() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> sessionService.declineSession(1L, "autre@ensias.ma"))
            .isInstanceOf(RuntimeException.class);
    }
}
