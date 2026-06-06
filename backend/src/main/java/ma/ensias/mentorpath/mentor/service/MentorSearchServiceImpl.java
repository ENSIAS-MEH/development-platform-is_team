package ma.ensias.mentorpath.mentor.service;

import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.mentor.dto.MentorSearchResponse;
import ma.ensias.mentorpath.mentor.repository.MentorSearchRepository;
import ma.ensias.mentorpath.user.entity.MentorProfile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de recherche de mentors.
 */
@Service
@RequiredArgsConstructor
public class MentorSearchServiceImpl implements MentorSearchService {

    private final MentorSearchRepository mentorSearchRepository;

    @Override
    public List<MentorSearchResponse> searchMentors(
            String filiere,
            BigDecimal minRating,
            Boolean available) {

        return mentorSearchRepository.searchMentors(filiere, minRating, available)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    private MentorSearchResponse toResponse(MentorProfile m) {
        return MentorSearchResponse.builder()
            .id(m.getId())
            .firstName(m.getFirstName())
            .lastName(m.getLastName())
            .email(m.getUser().getEmail())
            .filiere(m.getFiliere())
            .promo(m.getPromo())
            .bio(m.getBio())
            .expertise(m.getExpertise())
            .rating(m.getRating())
            .available(m.getAvailable())
            .build();
    }
}