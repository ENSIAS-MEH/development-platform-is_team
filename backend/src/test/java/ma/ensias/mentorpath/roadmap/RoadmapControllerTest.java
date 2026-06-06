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
    @DisplayName("enrollStudent() – doit inscrire un étudiant à une roadmap")
    void shouldEnrollStudent() {
        when(authentication.getName()).thenReturn("student@ensias.ma");
        when(roadmapService.enrollStudent(any(), anyString()))
            .thenReturn(RoadmapResponse.builder().id(1L).progressPercent(0).build());

        ResponseEntity<ApiResponse<RoadmapResponse>> result =
            roadmapController.enrollStudent(1L, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData().getProgressPercent()).isEqualTo(0);
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
}