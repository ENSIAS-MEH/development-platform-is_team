package ma.ensias.mentorpath.mentor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.common.ApiResponse;
import ma.ensias.mentorpath.mentor.dto.MentorSearchResponse;
import ma.ensias.mentorpath.mentor.service.MentorSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller REST pour la recherche de mentors.
 */
@RestController
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
@Tag(name = "Mentors", description = "Recherche de mentors")
public class MentorSearchController {

    private final MentorSearchService mentorSearchService;

    @Operation(summary = "Rechercher des mentors",
               description = "Filtres : filiere, minRating, available")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MentorSearchResponse>>> searchMentors(
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(required = false) Boolean available) {

        List<MentorSearchResponse> response =
            mentorSearchService.searchMentors(filiere, minRating, available);

        return ResponseEntity.ok(ApiResponse.success("Mentors trouvés", response));
    }
}