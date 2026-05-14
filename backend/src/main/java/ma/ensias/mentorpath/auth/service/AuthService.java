package ma.ensias.mentorpath.auth.service;

import ma.ensias.mentorpath.auth.dto.JwtResponse;
import ma.ensias.mentorpath.auth.dto.LoginRequest;
import ma.ensias.mentorpath.auth.dto.RegisterRequest;

/**
 * Interface du service d'authentification.
 *
 * Pattern Facade : expose deux opérations simples (register / login)
 * qui encapsulent toute la complexité (BCrypt, JWT, création de profil).
 *
 * Pattern Repository : cette interface dépend d'abstractions, pas d'implémentations (SOLID – D).
 */
public interface AuthService {

    /**
     * Inscrit un nouvel utilisateur et retourne un token JWT.
     *
     * @param request données d'inscription validées
     * @return token JWT + informations utilisateur
     */
    JwtResponse register(RegisterRequest request);

    /**
     * Connecte un utilisateur existant et retourne un token JWT.
     *
     * @param request email + mot de passe
     * @return token JWT + informations utilisateur
     */
    JwtResponse login(LoginRequest request);
}
