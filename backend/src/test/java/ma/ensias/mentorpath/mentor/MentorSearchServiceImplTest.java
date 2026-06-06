package ma.ensias.mentorpath.mentor;

import ma.ensias.mentorpath.mentor.dto.MentorSearchResponse;
import ma.ensias.mentorpath.mentor.repository.MentorSearchRepository;
import ma.ensias.mentorpath.mentor.service.MentorSearchServiceImpl;
import ma.ensias.mentorpath.user.entity.MentorProfile;
import ma.ensias.mentorpath.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.isNull;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MentorSearchServiceImpl - Tests unitaires")
class MentorSearchServiceImplTest {

    @Mock private MentorSearchRepository mentorSearchRepository;
    @InjectMocks private MentorSearchServiceImpl mentorSearchService;

    private User user;
    private MentorProfile mentorProfile;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("mentor@ensias.ma");

        mentorProfile = new MentorProfile();
        mentorProfile.setId(1L);
        mentorProfile.setFirstName("Ali");
        mentorProfile.setLastName("Benali");
        mentorProfile.setFiliere("2IA");
        mentorProfile.setPromo(2024);
        mentorProfile.setBio("Expert en IA");
        mentorProfile.setExpertise("Machine Learning");
        mentorProfile.setRating(new BigDecimal("4.5"));
        mentorProfile.setAvailable(true);
        mentorProfile.setUser(user);
    }

    @Test
    @DisplayName("searchMentors() - sans filtres, retourne tous les mentors")
    void shouldReturnAllMentorsWithoutFilters() {
        when(mentorSearchRepository.searchMentors(null, null, null))
            .thenReturn(List.of(mentorProfile));

        List<MentorSearchResponse> result = mentorSearchService.searchMentors(null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Ali");
        assertThat(result.get(0).getEmail()).isEqualTo("mentor@ensias.ma");
        assertThat(result.get(0).getFiliere()).isEqualTo("2IA");
    }

    @Test
    @DisplayName("searchMentors() - filtre par filiere")
    void shouldFilterByFiliere() {
        when(mentorSearchRepository.searchMentors(eq("2IA"), any(), any()))
            .thenReturn(List.of(mentorProfile));

        List<MentorSearchResponse> result = mentorSearchService.searchMentors("2IA", null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFiliere()).isEqualTo("2IA");
        verify(mentorSearchRepository).searchMentors("2IA", null, null);
    }


    @Test
    @DisplayName("searchMentors() - filtre par note minimale")
    void shouldFilterByMinRating() {
        BigDecimal minRating = new BigDecimal("4.0");
        when(mentorSearchRepository.searchMentors(isNull(), eq(minRating), isNull()))
            .thenReturn(List.of(mentorProfile));

        List<MentorSearchResponse> result = mentorSearchService.searchMentors(null, minRating, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRating()).isGreaterThanOrEqualTo(minRating);
    }

    @Test
    @DisplayName("searchMentors() - filtre disponibles uniquement")
    void shouldFilterByAvailability() {
        when(mentorSearchRepository.searchMentors(null, null, true))
            .thenReturn(List.of(mentorProfile));

        List<MentorSearchResponse> result = mentorSearchService.searchMentors(null, null, true);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAvailable()).isTrue();
    }

    @Test
    @DisplayName("searchMentors() - liste vide si aucun resultat")
    void shouldReturnEmptyList() {
        when(mentorSearchRepository.searchMentors(any(), any(), any())).thenReturn(List.of());

        List<MentorSearchResponse> result = mentorSearchService.searchMentors("UNKNOWN", null, null);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("searchMentors() - mapping complet des champs du profil")
    void shouldMapAllFieldsCorrectly() {
        when(mentorSearchRepository.searchMentors(null, null, null))
            .thenReturn(List.of(mentorProfile));

        List<MentorSearchResponse> result = mentorSearchService.searchMentors(null, null, null);

        MentorSearchResponse r = result.get(0);
        assertThat(r.getId()).isEqualTo(1L);
        assertThat(r.getLastName()).isEqualTo("Benali");
        assertThat(r.getBio()).isEqualTo("Expert en IA");
        assertThat(r.getExpertise()).isEqualTo("Machine Learning");
        assertThat(r.getPromo()).isEqualTo(2024);
    }
}
