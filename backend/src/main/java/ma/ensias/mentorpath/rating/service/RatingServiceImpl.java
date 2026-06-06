package ma.ensias.mentorpath.rating.service;

import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.exception.ResourceNotFoundException;
import ma.ensias.mentorpath.rating.dto.RatingRequest;
import ma.ensias.mentorpath.rating.dto.RatingResponse;
import ma.ensias.mentorpath.rating.entity.Rating;
import ma.ensias.mentorpath.rating.repository.RatingRepository;
import ma.ensias.mentorpath.user.entity.User;
import ma.ensias.mentorpath.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des évaluations mentors.
 */
@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    @Override
    public RatingResponse rateMentor(RatingRequest request, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Étudiant non trouvé"));

        User mentor = userRepository.findById(request.getMentorId())
            .orElseThrow(() -> new ResourceNotFoundException("Mentor non trouvé"));

        ratingRepository.findByStudentIdAndMentorId(student.getId(), mentor.getId())
            .ifPresent(r -> { throw new RuntimeException("Vous avez déjà évalué ce mentor"); });

        Rating rating = Rating.builder()
            .student(student)
            .mentor(mentor)
            .score(request.getScore())
            .comment(request.getComment())
            .build();

        return toResponse(ratingRepository.save(rating));
    }

    @Override
    public List<RatingResponse> getMentorRatings(Long mentorId) {
        return ratingRepository.findByMentorId(mentorId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    private RatingResponse toResponse(Rating rating) {
        return RatingResponse.builder()
            .id(rating.getId())
            .studentEmail(rating.getStudent().getEmail())
            .mentorEmail(rating.getMentor().getEmail())
            .score(rating.getScore())
            .comment(rating.getComment())
            .createdAt(rating.getCreatedAt())
            .build();
    }
}