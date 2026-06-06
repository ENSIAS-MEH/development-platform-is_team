package ma.ensias.mentorpath.mentor.service;

import ma.ensias.mentorpath.mentor.dto.MentorSearchResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface pour la recherche de mentors.
 */
public interface MentorSearchService {
    List<MentorSearchResponse> searchMentors(
        String filiere,
        BigDecimal minRating,
        Boolean available
    );
}