package ma.ensias.mentorpath.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.auth.dto.JwtResponse;
import ma.ensias.mentorpath.auth.dto.LoginRequest;
import ma.ensias.mentorpath.auth.dto.RegisterRequest;
import ma.ensias.mentorpath.auth.service.AuthService;
import ma.ensias.mentorpath.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur d'authentification.
 * Endpoints publics (pas de token requis) : /api/auth/register et /api/auth/login
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Inscription et connexion des utilisateurs")
public class AuthController {

    private final AuthService authService;

    /**
     * Inscription d'un nouvel utilisateur.
     * POST /api/auth/register
     */
    @PostMapping("/register")
    @Operation(summary = "Inscrire un nouvel utilisateur",
               description = "Crée un compte et retourne un token JWT")
    public ResponseEntity<ApiResponse<JwtResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        JwtResponse response = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inscription réussie", response));
    }

    /**
     * Connexion d'un utilisateur existant.
     * POST /api/auth/login
     */
    @PostMapping("/login")
    @Operation(summary = "Connecter un utilisateur",
               description = "Vérifie les identifiants et retourne un token JWT")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Connexion réussie", response));
    }
}
