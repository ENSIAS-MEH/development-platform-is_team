package ma.ensias.mentorpath.roadmap;

import ma.ensias.mentorpath.exception.ResourceNotFoundException;
import ma.ensias.mentorpath.roadmap.dto.RoadmapRequest;
import ma.ensias.mentorpath.roadmap.dto.RoadmapResponse;
import ma.ensias.mentorpath.roadmap.entity.Roadmap;
import ma.ensias.mentorpath.roadmap.entity.RoadmapEnrollment;
import ma.ensias.mentorpath.roadmap.entity.RoadmapStep;
import ma.ensias.mentorpath.roadmap.repository.RoadmapEnrollmentRepository;
import ma.ensias.mentorpath.roadmap.repository.RoadmapRepository;
import ma.ensias.mentorpath.roadmap.service.RoadmapServiceImpl;
import ma.ensias.mentorpath.user.entity.User;
import ma.ensias.mentorpath.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoadmapServiceImpl – Tests unitaires")
class RoadmapServiceImplTest {

    @Mock private RoadmapRepository roadmapRepository;
    @Mock private RoadmapEnrollmentRepository enrollmentRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private RoadmapServiceImpl roadmapService;

    private User mentor;
    private User student;
    private Roadmap roadmap;

    @BeforeEach
    void setUp() {
        mentor = new User();
        mentor.setId(1L);
        mentor.setEmail("mentor@ensias.ma");

        student = new User();
        student.setId(2L);
        student.setEmail("student@ensias.ma");

        roadmap = Roadmap.builder()
            .id(1L)
            .title("Roadmap 2IA")
            .description("Guide complet")
            .filiere("2IA")
            .mentor(mentor)
            .steps(new ArrayList<>())
            .build();
    }

    // ──────────────────────────────────────────────────────────────
    // createRoadmap
    // ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("createRoadmap() – cas nominal sans étapes")
    void shouldCreateRoadmapWithoutSteps() {
        RoadmapRequest request = RoadmapRequest.builder()
            .title("Roadmap 2IA")
            .description("Guide complet")
            .filiere("2IA")
            .build();

        when(userRepository.findByEmail("mentor@ensias.ma")).thenReturn(Optional.of(mentor));
        when(roadmapRepository.save(any())).thenReturn(roadmap);
        when(enrollmentRepository.findByRoadmapId(any())).thenReturn(List.of());

        RoadmapResponse result = roadmapService.createRoadmap(request, "mentor@ensias.ma");

        assertThat(result.getTitle()).isEqualTo("Roadmap 2IA");
        assertThat(result.getFiliere()).isEqualTo("2IA");
        verify(roadmapRepository).save(any());
    }

    @Test
    @DisplayName("createRoadmap() – cas nominal avec étapes")
    void shouldCreateRoadmapWithSteps() {
        List<RoadmapRequest.StepRequest> steps = List.of(
            new RoadmapRequest.StepRequest("Étape 1", "Intro", 1),
            new RoadmapRequest.StepRequest("Étape 2", "Avancé", 2)
        );
        RoadmapRequest request = RoadmapRequest.builder()
            .title("Roadmap GL")
            .filiere("GL")
            .steps(steps)
            .build();

        Roadmap savedRoadmap = Roadmap.builder()
            .id(2L).title("Roadmap GL").filiere("GL").mentor(mentor)
            .steps(List.of(
                RoadmapStep.builder().id(1L).title("Étape 1").stepOrder(1).build(),
                RoadmapStep.builder().id(2L).title("Étape 2").stepOrder(2).build()
            ))
            .build();

        when(userRepository.findByEmail("mentor@ensias.ma")).thenReturn(Optional.of(mentor));
        when(roadmapRepository.save(any())).thenReturn(savedRoadmap);
        when(enrollmentRepository.findByRoadmapId(any())).thenReturn(List.of());

        RoadmapResponse result = roadmapService.createRoadmap(request, "mentor@ensias.ma");

        assertThat(result.getSteps()).hasSize(2);
    }

    @Test
    @DisplayName("createRoadmap() – mentor introuvable → ResourceNotFoundException")
    void shouldThrowWhenMentorNotFoundOnCreate() {
        when(userRepository.findByEmail("inconnu@ensias.ma")).thenReturn(Optional.empty());

        RoadmapRequest request = RoadmapRequest.builder().title("X").build();

        assertThatThrownBy(() -> roadmapService.createRoadmap(request, "inconnu@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ──────────────────────────────────────────────────────────────
    // updateRoadmap
    // ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("updateRoadmap() – cas nominal")
    void shouldUpdateRoadmap() {
        RoadmapRequest request = RoadmapRequest.builder()
            .title("Roadmap 2IA v2")
            .description("Mise à jour")
            .filiere("2IA")
            .build();

        when(roadmapRepository.findById(1L)).thenReturn(Optional.of(roadmap));
        when(roadmapRepository.save(any())).thenReturn(roadmap);
        when(enrollmentRepository.findByRoadmapId(any())).thenReturn(List.of());

        RoadmapResponse result = roadmapService.updateRoadmap(1L, request, "mentor@ensias.ma");

        assertThat(result).isNotNull();
        verify(roadmapRepository).save(roadmap);
    }

    @Test
    @DisplayName("updateRoadmap() – roadmap introuvable → ResourceNotFoundException")
    void shouldThrowWhenRoadmapNotFoundOnUpdate() {
        when(roadmapRepository.findById(99L)).thenReturn(Optional.empty());

        RoadmapRequest request = RoadmapRequest.builder().title("X").build();

        assertThatThrownBy(() -> roadmapService.updateRoadmap(99L, request, "mentor@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("updateRoadmap() – mauvais mentor → RuntimeException")
    void shouldThrowWhenWrongMentorOnUpdate() {
        when(roadmapRepository.findById(1L)).thenReturn(Optional.of(roadmap));

        RoadmapRequest request = RoadmapRequest.builder().title("X").build();

        assertThatThrownBy(() -> roadmapService.updateRoadmap(1L, request, "autre@ensias.ma"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("propriétaire");
    }

    // ──────────────────────────────────────────────────────────────
    // deleteRoadmap
    // ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("deleteRoadmap() – cas nominal")
    void shouldDeleteRoadmap() {
        when(roadmapRepository.findById(1L)).thenReturn(Optional.of(roadmap));

        roadmapService.deleteRoadmap(1L, "mentor@ensias.ma");

        verify(roadmapRepository).delete(roadmap);
    }

    @Test
    @DisplayName("deleteRoadmap() – roadmap introuvable → ResourceNotFoundException")
    void shouldThrowWhenRoadmapNotFoundOnDelete() {
        when(roadmapRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roadmapService.deleteRoadmap(99L, "mentor@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("deleteRoadmap() – mauvais mentor → RuntimeException")
    void shouldThrowWhenWrongMentorOnDelete() {
        when(roadmapRepository.findById(1L)).thenReturn(Optional.of(roadmap));

        assertThatThrownBy(() -> roadmapService.deleteRoadmap(1L, "autre@ensias.ma"))
            .isInstanceOf(RuntimeException.class);
    }

    // ──────────────────────────────────────────────────────────────
    // getAllRoadmaps
    // ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAllRoadmaps() – doit retourner la liste complète")
    void shouldGetAllRoadmaps() {
        when(roadmapRepository.findAll()).thenReturn(List.of(roadmap));
        when(enrollmentRepository.findByRoadmapId(any())).thenReturn(List.of());

        List<RoadmapResponse> result = roadmapService.getAllRoadmaps(null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Roadmap 2IA");
    }

    @Test
    @DisplayName("getAllRoadmaps() – liste vide si aucune roadmap")
    void shouldReturnEmptyListWhenNoRoadmaps() {
        when(roadmapRepository.findByFiliere("inconnu")).thenReturn(List.of());

        List<RoadmapResponse> result = roadmapService.getAllRoadmaps("inconnu", null);

        assertThat(result).isEmpty();
    }

    // ──────────────────────────────────────────────────────────────
    // getRoadmapById
    // ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getRoadmapById() – cas nominal")
    void shouldGetRoadmapById() {
        when(roadmapRepository.findById(1L)).thenReturn(Optional.of(roadmap));
        when(enrollmentRepository.findByRoadmapId(1L)).thenReturn(List.of());

        RoadmapResponse result = roadmapService.getRoadmapById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getMentorName()).isEqualTo("mentor@ensias.ma");
    }

    @Test
    @DisplayName("getRoadmapById() – introuvable → ResourceNotFoundException")
    void shouldThrowWhenRoadmapNotFoundById() {
        when(roadmapRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roadmapService.getRoadmapById(99L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ──────────────────────────────────────────────────────────────
    // enrollStudent
    // ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("enrollStudent() – cas nominal")
    void shouldEnrollStudent() {
        RoadmapEnrollment enrollment = RoadmapEnrollment.builder()
            .id(1L).student(student).roadmap(roadmap).progressPercent(0).build();

        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(roadmapRepository.findById(1L)).thenReturn(Optional.of(roadmap));
        when(enrollmentRepository.findByStudentIdAndRoadmapId(2L, 1L)).thenReturn(Optional.empty());
        when(enrollmentRepository.save(any())).thenReturn(enrollment);
        when(enrollmentRepository.findByRoadmapId(1L)).thenReturn(List.of(enrollment));

        RoadmapResponse result = roadmapService.enrollStudent(1L, "student@ensias.ma");

        assertThat(result.getProgressPercent()).isEqualTo(0);
        verify(enrollmentRepository).save(any());
    }

    @Test
    @DisplayName("enrollStudent() – déjà inscrit → RuntimeException")
    void shouldThrowWhenAlreadyEnrolled() {
        RoadmapEnrollment existing = RoadmapEnrollment.builder().id(1L).build();

        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(roadmapRepository.findById(1L)).thenReturn(Optional.of(roadmap));
        when(enrollmentRepository.findByStudentIdAndRoadmapId(2L, 1L))
            .thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> roadmapService.enrollStudent(1L, "student@ensias.ma"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Déjà inscrit");
    }

    @Test
    @DisplayName("enrollStudent() – étudiant introuvable → ResourceNotFoundException")
    void shouldThrowWhenStudentNotFoundOnEnroll() {
        when(userRepository.findByEmail("inconnu@ensias.ma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roadmapService.enrollStudent(1L, "inconnu@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    // ──────────────────────────────────────────────────────────────
    // getMyProgress
    // ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getMyProgress() – doit retourner les progressions de l'étudiant")
    void shouldGetMyProgress() {
        RoadmapEnrollment e1 = RoadmapEnrollment.builder()
            .student(student).roadmap(roadmap).progressPercent(50).build();

        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(enrollmentRepository.findByStudentId(2L)).thenReturn(List.of(e1));
        when(enrollmentRepository.findByRoadmapId(any())).thenReturn(List.of(e1));

        List<RoadmapResponse> result = roadmapService.getMyProgress("student@ensias.ma");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProgressPercent()).isEqualTo(50);
    }

    @Test
    @DisplayName("getMyProgress() – étudiant introuvable → ResourceNotFoundException")
    void shouldThrowWhenStudentNotFoundOnProgress() {
        when(userRepository.findByEmail("inconnu@ensias.ma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roadmapService.getMyProgress("inconnu@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}
