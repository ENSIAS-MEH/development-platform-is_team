package ma.ensias.mentorpath.rating;

import ma.ensias.mentorpath.common.ApiResponse;
import ma.ensias.mentorpath.rating.controller.RatingController;
import ma.ensias.mentorpath.rating.dto.RatingRequest;
import ma.ensias.mentorpath.rating.dto.RatingResponse;
import ma.ensias.mentorpath.rating.service.RatingService;
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

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("RatingController – Tests unitaires")
class RatingControllerTest {

    @Mock private RatingService ratingService;
    @Mock private Authentication authentication;
    @InjectMocks private RatingController ratingController;

    @BeforeEach
    void setUp() {
        when(authentication.getName()).thenReturn("student@ensias.ma");
    }

    @Test
    @DisplayName("rateMentor() – doit noter un mentor avec succès")
    void shouldRateMentor() {
        RatingRequest request = RatingRequest.builder()
            .mentorId(1L)
            .score(5)
            .comment("Excellent mentor!")
            .build();

        RatingResponse response = RatingResponse.builder()
            .id(1L)
            .score(5)
            .comment("Excellent mentor!")
            .studentEmail("student@ensias.ma")
            .mentorEmail("mentor@ensias.ma")
            .build();

        when(ratingService.rateMentor(any(), eq("student@ensias.ma"))).thenReturn(response);

        ResponseEntity<ApiResponse<RatingResponse>> result =
            ratingController.rateMentor(request, authentication);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData().getScore()).isEqualTo(5);
        assertThat(result.getBody().getData().getComment()).isEqualTo("Excellent mentor!");
        verify(ratingService).rateMentor(any(), eq("student@ensias.ma"));
    }

    @Test
    @DisplayName("getMentorRatings() – doit retourner les notes d'un mentor")
    void shouldGetMentorRatings() {
        when(ratingService.getMentorRatings(1L))
            .thenReturn(List.of(
                RatingResponse.builder().id(1L).score(5).build(),
                RatingResponse.builder().id(2L).score(4).build()
            ));

        ResponseEntity<ApiResponse<List<RatingResponse>>> result =
            ratingController.getMentorRatings(1L);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData()).hasSize(2);
        assertThat(result.getBody().getData().get(0).getScore()).isEqualTo(5);
        verify(ratingService).getMentorRatings(1L);
    }

    @Test
    @DisplayName("getMentorRatings() – doit retourner liste vide si aucune note")
    void shouldReturnEmptyListWhenNoRatings() {
        when(ratingService.getMentorRatings(99L)).thenReturn(List.of());

        ResponseEntity<ApiResponse<List<RatingResponse>>> result =
            ratingController.getMentorRatings(99L);

        assertThat(result.getBody().getData()).isEmpty();
    }
}
