package ma.ensias.mentorpath.auth.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.auth.dto.JwtResponse;
import ma.ensias.mentorpath.auth.dto.LoginRequest;
import ma.ensias.mentorpath.auth.dto.RegisterRequest;
import ma.ensias.mentorpath.auth.service.AuthService;
import ma.ensias.mentorpath.exception.EmailAlreadyUsedException;
import ma.ensias.mentorpath.security.JwtUtil;
import ma.ensias.mentorpath.user.entity.*;
import ma.ensias.mentorpath.user.repository.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service d'authentification.
 *
 * Responsabilités :
 *  1. Valider l'unicité de l'email
 *  2. Hasher le mot de passe (BCrypt)
 *  3. Créer l'entité User en base
 *  4. Créer le profil complémentaire (Student ou Mentor)
 *  5. Générer et retourner le token JWT
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository           userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final MentorProfileRepository  mentorProfileRepository;
    private final PasswordEncoder          passwordEncoder;
    private final JwtUtil                  jwtUtil;
    private final AuthenticationManager   authenticationManager;

    // ── Register ─────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public JwtResponse register(RegisterRequest req) {

        // 1. Vérifier l'unicité de l'email
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new EmailAlreadyUsedException(req.getEmail());
        }

        // 2. Créer et sauvegarder l'utilisateur
        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();
        userRepository.save(user);

        // 3. Créer le profil selon le rôle
        createProfile(user, req);

        // 4. Générer le token JWT
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new JwtResponse(token, user.getEmail(), user.getRole().name());
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    @Override
    public JwtResponse login(LoginRequest req) {

        // Spring Security vérifie l'email ET le mot de passe (BCrypt)
      
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        User user = userRepository.findByEmail(req.getEmail()).orElseThrow();

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new JwtResponse(token, user.getEmail(), user.getRole().name());
    }

    // ── Privé ─────────────────────────────────────────────────────────────────

    /**
     * Crée le profil complémentaire selon le rôle de l'utilisateur.
     * ADMIN n'a pas de profil complémentaire.
     */
    private void createProfile(User user, RegisterRequest req) {
        switch (user.getRole()) {
            case STUDENT -> {
                StudentProfile profile = StudentProfile.builder()
                        .user(user)
                        .firstName(req.getFirstName())
                        .lastName(req.getLastName())
                        .filiere(req.getFiliere())
                        .anneeEtude(req.getAnneeEtude())
                        .build();
                studentProfileRepository.save(profile);
            }
            case MENTOR -> {
                MentorProfile profile = MentorProfile.builder()
                        .user(user)
                        .firstName(req.getFirstName())
                        .lastName(req.getLastName())
                        .filiere(req.getFiliere())
                        .promo(req.getPromo())
                        .build();
                mentorProfileRepository.save(profile);
            }
            case ADMIN -> { /* Pas de profil complémentaire pour les admins */ }
        }
    }
}
