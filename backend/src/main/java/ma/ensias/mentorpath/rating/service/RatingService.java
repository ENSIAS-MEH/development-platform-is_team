package ma.ensias.mentorpath.rating.service;

import ma.ensias.mentorpath.rating.dto.RatingRequest;
import ma.ensias.mentorpath.rating.dto.RatingResponse;

import java.util.List;

/**
 * Service interface pour la gestion des évaluations.
 */
public interface RatingService {
    RatingResponse rateMentor(RatingRequest request, String studentEmail);
    List<RatingResponse> getMentorRatings(Long mentorId);
}