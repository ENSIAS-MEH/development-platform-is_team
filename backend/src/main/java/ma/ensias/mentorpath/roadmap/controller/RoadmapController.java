package ma.ensias.mentorpath.roadmap.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.common.ApiResponse;
import ma.ensias.mentorpath.roadmap.dto.RoadmapRequest;
import ma.ensias.mentorpath.roadmap.dto.RoadmapResponse;
import ma.ensias.mentorpath.roadmap.service.RoadmapService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour la gestion des roadmaps.
 * Expose les endpoints CRUD et de suivi des roadmaps.
 */
@RestController
@RequestMapping("/api/roadmaps")
@RequiredArgsConstructor
@Tag(name = "Roadmaps", description = "Gestion des roadmaps de mentorat")
public class RoadmapController {

    private final RoadmapService roadmapService;

    @Operation(summary = "Créer une roadmap", description = "Réservé aux MENTORs")
    @PostMapping
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse<RoadmapResponse>> createRoadmap(
            @Valid @RequestBody RoadmapRequest request,
            Authentication auth) {
        RoadmapResponse response = roadmapService.createRoadmap(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Roadmap créée avec succès", response));
    }

    @Operation(summary = "Voir toutes les roadmaps")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoadmapResponse>>> getAllRoadmaps(
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) String keyword) {
        List<RoadmapResponse> response = roadmapService.getAllRoadmaps(filiere, keyword);
        return ResponseEntity.ok(ApiResponse.success("Roadmaps récupérées", response));
    }

    @Operation(summary = "Voir une roadmap par ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoadmapResponse>> getRoadmapById(@PathVariable Long id) {
        RoadmapResponse response = roadmapService.getRoadmapById(id);
        return ResponseEntity.ok(ApiResponse.success("Roadmap récupérée", response));
    }

    @Operation(summary = "Modifier une roadmap", description = "Réservé au MENTOR propriétaire")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse<RoadmapResponse>> updateRoadmap(
            @PathVariable Long id,
            @Valid @RequestBody RoadmapRequest request,
            Authentication auth) {
        RoadmapResponse response = roadmapService.updateRoadmap(id, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Roadmap modifiée avec succès", response));
    }

    @Operation(summary = "Supprimer une roadmap", description = "Réservé au MENTOR propriétaire")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse<Void>> deleteRoadmap(
            @PathVariable Long id,
            Authentication auth) {
        roadmapService.deleteRoadmap(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Roadmap supprimée", null));
    }

    @Operation(summary = "S'inscrire à une roadmap", description = "Réservé aux STUDENTs")
    @PostMapping("/{id}/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<RoadmapResponse>> enrollStudent(
            @PathVariable Long id,
            Authentication auth) {
        RoadmapResponse response = roadmapService.enrollStudent(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Inscription réussie", response));
    }

    @Operation(summary = "Voir ma progression", description = "Réservé aux STUDENTs")
    @GetMapping("/my-progress")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<RoadmapResponse>>> getMyProgress(
            Authentication auth) {
        List<RoadmapResponse> response = roadmapService.getMyProgress(auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Progression récupérée", response));
    }
}