package ma.ensias.mentorpath.roadmap;

import ma.ensias.mentorpath.roadmap.dto.RoadmapRequest;
import ma.ensias.mentorpath.roadmap.dto.RoadmapResponse;
import ma.ensias.mentorpath.roadmap.service.RoadmapService;
import ma.ensias.mentorpath.roadmap.controller.RoadmapController;
import ma.ensias.mentorpath.common.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("RoadmapController – Tests unitaires")
class RoadmapControllerTest {

    @Mock private RoadmapService roadmapService;
    @Mock private Authentication authentication;
    @InjectMocks private RoadmapController roadmapController;

    @BeforeEach
    void setUp() {
        when(authentication.getName()).thenReturn("mentor@ensias.ma");
    }

    @Test
    @DisplayName("createRoadmap() – doit retourner 200 avec la roadmap créée")
    void shouldCreateRoadmap() {
        RoadmapRequest request = RoadmapRequest.builder()
            .title("Roadmap 2IA")
            .description("Guide complet")
            .filiere("2IA")
            .build();

        RoadmapResponse response = RoadmapResponse.builder()
            .id(1L)
            .title("Roadmap 2IA")
            .filiere("2IA")
            .build();

        when(roadmapService.createRoadmap(any(), anyString())).thenReturn(response);

        ResponseEntity<ApiResponse<RoadmapResponse>> result =
            roadmapController.createRoadmap(request, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData().getTitle()).isEqualTo("Roadmap 2IA");
        verify(roadmapService).createRoadmap(any(), eq("mentor@ensias.ma"));
    }

    @Test
    @DisplayName("getAllRoadmaps() – doit retourner la liste des roadmaps")
    void shouldGetAllRoadmaps() {
        when(roadmapService.getAllRoadmaps(any(), any()))
            .thenReturn(List.of(
                RoadmapResponse.builder().id(1L).title("Roadmap 2IA").build(),
                RoadmapResponse.builder().id(2L).title("Roadmap GL").build()
            ));

        ResponseEntity<ApiResponse<List<RoadmapResponse>>> result =
            roadmapController.getAllRoadmaps(null, null);

        assertThat(result.getBody().getData()).hasSize(2);
    }

    @Test
    @DisplayName("getAllRoadmaps() – doit filtrer par filiere et keyword")
    void shouldGetAllRoadmapsWithFilters() {
        when(roadmapService.getAllRoadmaps(eq("2IA"), eq("java")))
            .thenReturn(List.of(
                RoadmapResponse.builder().id(1L).title("Java 2IA").filiere("2IA").build()
            ));

        ResponseEntity<ApiResponse<List<RoadmapResponse>>> result =
            roadmapController.getAllRoadmaps("2IA", "java");

        assertThat(result.getBody().getData()).hasSize(1);
        assertThat(result.getBody().getData().get(0).getFiliere()).isEqualTo("2IA");
    }

    @Test
    @DisplayName("getRoadmapById() – doit retourner une roadmap par son ID")
    void shouldGetRoadmapById() {
        RoadmapResponse response = RoadmapResponse.builder()
            .id(1L)
            .title("Roadmap 2IA")
            .filiere("2IA")
            .build();

        when(roadmapService.getRoadmapById(1L)).thenReturn(response);

        ResponseEntity<ApiResponse<RoadmapResponse>> result =
            roadmapController.getRoadmapById(1L);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData().getId()).isEqualTo(1L);
        verify(roadmapService).getRoadmapById(1L);
    }

    @Test
    @DisplayName("updateRoadmap() – doit modifier une roadmap existante")
    void shouldUpdateRoadmap() {
        RoadmapRequest request = RoadmapRequest.builder()
            .title("Roadmap 2IA – v2")
            .description("Mise à jour")
            .filiere("2IA")
            .build();

        RoadmapResponse response = RoadmapResponse.builder()
            .id(1L)
            .title("Roadmap 2IA – v2")
            .filiere("2IA")
            .build();

        when(roadmapService.updateRoadmap(eq(1L), any(), eq("mentor@ensias.ma")))
            .thenReturn(response);

        ResponseEntity<ApiResponse<RoadmapResponse>> result =
            roadmapController.updateRoadmap(1L, request, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData().getTitle()).isEqualTo("Roadmap 2IA – v2");
        verify(roadmapService).updateRoadmap(eq(1L), any(), eq("mentor@ensias.ma"));
    }

    @Test
    @DisplayName("deleteRoadmap() – doit supprimer une roadmap")
    void shouldDeleteRoadmap() {
        doNothing().when(roadmapService).deleteRoadmap(any(), anyString());

        ResponseEntity<ApiResponse<Void>> result =
            roadmapController.deleteRoadmap(1L, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        verify(roadmapService).deleteRoadmap(eq(1L), eq("mentor@ensias.ma"));
    }

    @Test
    @DisplayName("enrollStudent() – doit inscrire un étudiant à une roadmap")
    void shouldEnrollStudent() {
        when(authentication.getName()).thenReturn("student@ensias.ma");
        when(roadmapService.enrollStudent(eq(1L), eq("student@ensias.ma")))
            .thenReturn(RoadmapResponse.builder().id(1L).progressPercent(0).build());

        ResponseEntity<ApiResponse<RoadmapResponse>> result =
            roadmapController.enrollStudent(1L, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData().getProgressPercent()).isEqualTo(0);
        verify(roadmapService).enrollStudent(eq(1L), eq("student@ensias.ma"));
    }

    @Test
    @DisplayName("getMyProgress() – doit retourner la progression de l'étudiant connecté")
    void shouldGetMyProgress() {
        when(authentication.getName()).thenReturn("student@ensias.ma");
        when(roadmapService.getMyProgress("student@ensias.ma"))
            .thenReturn(List.of(
                RoadmapResponse.builder().id(1L).progressPercent(40).build(),
                RoadmapResponse.builder().id(2L).progressPercent(75).build()
            ));

        ResponseEntity<ApiResponse<List<RoadmapResponse>>> result =
            roadmapController.getMyProgress(authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData()).hasSize(2);
        assertThat(result.getBody().getData().get(1).getProgressPercent()).isEqualTo(75);
        verify(roadmapService).getMyProgress("student@ensias.ma");
    }
}
