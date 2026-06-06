package ma.ensias.mentorpath.session.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.common.ApiResponse;
import ma.ensias.mentorpath.session.dto.SessionRequest;
import ma.ensias.mentorpath.session.dto.SessionResponse;
import ma.ensias.mentorpath.session.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour la gestion des sessions de mentorat.
 * Expose les endpoints pour les demandes et la planification.
 */
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Tag(name = "Sessions", description = "Gestion des sessions de mentorat")
public class SessionController {

    private final SessionService sessionService;

    @Operation(summary = "Demander une session", description = "Réservé aux STUDENTs")
    @PostMapping("/request")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<SessionResponse>> requestSession(
            @Valid @RequestBody SessionRequest request,
            Authentication auth) {
        SessionResponse response = sessionService.requestSession(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Session demandée avec succès", response));
    }

    @Operation(summary = "Voir mes sessions", description = "Réservé aux STUDENTs")
    @GetMapping("/my-sessions")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getMySessions(
            Authentication auth) {
        List<SessionResponse> response = sessionService.getMySessions(auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Sessions récupérées", response));
    }

    @Operation(summary = "Voir mon planning", description = "Réservé aux MENTORs")
    @GetMapping("/mentor-schedule")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getMentorSchedule(
            Authentication auth) {
        List<SessionResponse> response = sessionService.getMentorSchedule(auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Planning récupéré", response));
    }

    @Operation(summary = "Accepter une session", description = "Réservé aux MENTORs")
    @PutMapping("/{id}/accept")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse<SessionResponse>> acceptSession(
            @PathVariable Long id,
            Authentication auth) {
        SessionResponse response = sessionService.acceptSession(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Session acceptée", response));
    }

    @Operation(summary = "Refuser une session", description = "Réservé aux MENTORs")
    @PutMapping("/{id}/decline")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApiResponse<SessionResponse>> declineSession(
            @PathVariable Long id,
            Authentication auth) {
        SessionResponse response = sessionService.declineSession(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Session refusée", response));
    }
}