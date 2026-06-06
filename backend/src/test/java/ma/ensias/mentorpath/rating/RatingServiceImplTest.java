package ma.ensias.mentorpath.rating;

import ma.ensias.mentorpath.exception.ResourceNotFoundException;
import ma.ensias.mentorpath.rating.dto.RatingRequest;
import ma.ensias.mentorpath.rating.dto.RatingResponse;
import ma.ensias.mentorpath.rating.entity.Rating;
import ma.ensias.mentorpath.rating.repository.RatingRepository;
import ma.ensias.mentorpath.rating.service.RatingServiceImpl;
import ma.ensias.mentorpath.user.entity.User;
import ma.ensias.mentorpath.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RatingServiceImpl – Tests unitaires")
class RatingServiceImplTest {

    @Mock private RatingRepository ratingRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private RatingServiceImpl ratingService;

    private User student;
    private User mentor;
    private Rating rating;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setEmail("student@ensias.ma");

        mentor = new User();
        mentor.setId(2L);
        mentor.setEmail("mentor@ensias.ma");

        rating = Rating.builder()
            .id(1L)
            .student(student)
            .mentor(mentor)
            .score(5)
            .comment("Excellent!")
            .build();
    }

    @Test
    @DisplayName("rateMentor() – cas nominal")
    void shouldRateMentor() {
        RatingRequest request = RatingRequest.builder()
            .mentorId(2L).score(5).comment("Excellent!").build();

        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mentor));
        when(ratingRepository.findByStudentIdAndMentorId(1L, 2L)).thenReturn(Optional.empty());
        when(ratingRepository.save(any())).thenReturn(rating);

        RatingResponse result = ratingService.rateMentor(request, "student@ensias.ma");

        assertThat(result.getScore()).isEqualTo(5);
        assertThat(result.getStudentEmail()).isEqualTo("student@ensias.ma");
        verify(ratingRepository).save(any());
    }

    @Test
    @DisplayName("rateMentor() – déjà noté → RuntimeException")
    void shouldThrowWhenAlreadyRated() {
        RatingRequest request = RatingRequest.builder().mentorId(2L).score(3).build();

        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mentor));
        when(ratingRepository.findByStudentIdAndMentorId(1L, 2L))
            .thenReturn(Optional.of(rating));

        assertThatThrownBy(() -> ratingService.rateMentor(request, "student@ensias.ma"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("déjà évalué");
    }

    @Test
    @DisplayName("rateMentor() – étudiant introuvable → ResourceNotFoundException")
    void shouldThrowWhenStudentNotFound() {
        when(userRepository.findByEmail("inconnu@ensias.ma")).thenReturn(Optional.empty());
        RatingRequest request = RatingRequest.builder().mentorId(2L).score(4).build();

        assertThatThrownBy(() -> ratingService.rateMentor(request, "inconnu@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("rateMentor() – mentor introuvable → ResourceNotFoundException")
    void shouldThrowWhenMentorNotFound() {
        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(student));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        RatingRequest request = RatingRequest.builder().mentorId(99L).score(4).build();

        assertThatThrownBy(() -> ratingService.rateMentor(request, "student@ensias.ma"))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getMentorRatings() – doit retourner les notes du mentor")
    void shouldGetMentorRatings() {
        when(ratingRepository.findByMentorId(2L)).thenReturn(List.of(rating));

        List<RatingResponse> result = ratingService.getMentorRatings(2L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getScore()).isEqualTo(5);
        assertThat(result.get(0).getMentorEmail()).isEqualTo("mentor@ensias.ma");
    }

    @Test
    @DisplayName("getMentorRatings() – liste vide si aucune note")
    void shouldReturnEmptyWhenNoRatings() {
        when(ratingRepository.findByMentorId(99L)).thenReturn(List.of());

        List<RatingResponse> result = ratingService.getMentorRatings(99L);

        assertThat(result).isEmpty();
    }
}
