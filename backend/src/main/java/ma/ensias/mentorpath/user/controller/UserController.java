package ma.ensias.mentorpath.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.common.ApiResponse;
import ma.ensias.mentorpath.user.dto.UpdateProfileRequest;
import ma.ensias.mentorpath.user.dto.UserDTO;
import ma.ensias.mentorpath.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur de gestion des profils utilisateurs.
 * Tous les endpoints nécessitent un token JWT valide.
 *
 * Authentication auth → Spring Security injecte automatiquement
 * l'utilisateur connecté depuis le SecurityContext.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Profil utilisateur", description = "Lecture et mise à jour du profil")
@SecurityRequirement(name = "BearerAuth")
public class UserController {

    private final UserService userService;

    /**
     * Récupère le profil de l'utilisateur connecté.
     * GET /api/users/me
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('STUDENT', 'MENTOR', 'ADMIN')")
    @Operation(summary = "Voir mon profil")
    public ResponseEntity<ApiResponse<UserDTO>> getMyProfile(Authentication auth) {
        UserDTO profile = userService.getProfile(auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Profil récupéré", profile));
    }

    /**
     * Met à jour le profil de l'utilisateur connecté.
     * PUT /api/users/me
     */
    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('STUDENT', 'MENTOR')")
    @Operation(summary = "Modifier mon profil")
    public ResponseEntity<ApiResponse<UserDTO>> updateMyProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication auth) {

        UserDTO updated = userService.updateProfile(auth.getName(), request);
        return ResponseEntity.ok(ApiResponse.success("Profil mis à jour", updated));
    }
}
