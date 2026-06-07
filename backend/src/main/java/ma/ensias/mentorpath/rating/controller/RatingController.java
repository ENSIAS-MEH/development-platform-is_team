package ma.ensias.mentorpath.rating.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.common.ApiResponse;
import ma.ensias.mentorpath.rating.dto.RatingRequest;
import ma.ensias.mentorpath.rating.dto.RatingResponse;
import ma.ensias.mentorpath.rating.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour la gestion des évaluations mentors.
 */
@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@Tag(name = "Ratings", description = "Évaluation des mentors")
public class RatingController {

    private final RatingService ratingService;

    @Operation(summary = "Noter un mentor", description = "Réservé aux STUDENTs")
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<RatingResponse>> rateMentor(
            @Valid @RequestBody RatingRequest request,
            Authentication auth) {
        RatingResponse response = ratingService.rateMentor(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Évaluation ajoutée", response));
    }

    @Operation(summary = "Voir les notes d'un mentor")
    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<ApiResponse<List<RatingResponse>>> getMentorRatings(
            @PathVariable Long mentorId) {
        List<RatingResponse> response = ratingService.getMentorRatings(mentorId);
        return ResponseEntity.ok(ApiResponse.success("Évaluations récupérées", response));
    }
}