package ma.ensias.mentorpath.mentor;

import ma.ensias.mentorpath.common.ApiResponse;
import ma.ensias.mentorpath.mentor.controller.MentorSearchController;
import ma.ensias.mentorpath.mentor.dto.MentorSearchResponse;
import ma.ensias.mentorpath.mentor.service.MentorSearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.mockito.ArgumentMatchers.isNull;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MentorSearchController - Tests unitaires")
class MentorSearchControllerTest {

    @Mock private MentorSearchService mentorSearchService;
    @InjectMocks private MentorSearchController mentorSearchController;

    @Test
    @DisplayName("searchMentors() - sans filtres, retourne tous les mentors")
    void shouldSearchMentorsWithoutFilters() {
        when(mentorSearchService.searchMentors(null, null, null))
            .thenReturn(List.of(
                MentorSearchResponse.builder().id(1L).firstName("Ali").filiere("2IA").build(),
                MentorSearchResponse.builder().id(2L).firstName("Sara").filiere("GL").build()
            ));

        ResponseEntity<ApiResponse<List<MentorSearchResponse>>> result =
            mentorSearchController.searchMentors(null, null, null);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody().getData()).hasSize(2);
        verify(mentorSearchService).searchMentors(null, null, null);
    }

    @Test
    @DisplayName("searchMentors() - filtre par filiere")
    void shouldSearchMentorsByFiliere() {
        when(mentorSearchService.searchMentors(eq("2IA"), any(), any()))
            .thenReturn(List.of(
                MentorSearchResponse.builder().id(1L).filiere("2IA").available(true).build()
            ));

        ResponseEntity<ApiResponse<List<MentorSearchResponse>>> result =
            mentorSearchController.searchMentors("2IA", null, null);

        assertThat(result.getBody().getData()).hasSize(1);
        assertThat(result.getBody().getData().get(0).getFiliere()).isEqualTo("2IA");
    }

   @Test
    @DisplayName("searchMentors() - filtre par note minimale et disponibilite")
    void shouldSearchMentorsByRatingAndAvailability() {
    BigDecimal minRating = new BigDecimal("4.0");
    when(mentorSearchService.searchMentors(isNull(), eq(minRating), eq(true)))
        .thenReturn(List.of(
            MentorSearchResponse.builder()
                .id(1L).rating(new BigDecimal("4.5")).available(true).build()
        ));

    ResponseEntity<ApiResponse<List<MentorSearchResponse>>> result =
        mentorSearchController.searchMentors(null, minRating, true);

    assertThat(result.getBody().getData()).hasSize(1);
    assertThat(result.getBody().getData().get(0).getAvailable()).isTrue();
}
    
    @Test
    @DisplayName("searchMentors() - liste vide si aucun mentor ne correspond")
    void shouldReturnEmptyWhenNoMentorsMatch() {
        when(mentorSearchService.searchMentors(eq("UNKNOWN"), any(), any()))
            .thenReturn(List.of());

        ResponseEntity<ApiResponse<List<MentorSearchResponse>>> result =
            mentorSearchController.searchMentors("UNKNOWN", null, null);

        assertThat(result.getBody().getData()).isEmpty();
    }
}
