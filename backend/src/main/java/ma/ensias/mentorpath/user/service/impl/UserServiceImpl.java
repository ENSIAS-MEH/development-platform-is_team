package ma.ensias.mentorpath.user.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.exception.ResourceNotFoundException;
import ma.ensias.mentorpath.user.dto.UpdateProfileRequest;
import ma.ensias.mentorpath.user.dto.UserDTO;
import ma.ensias.mentorpath.user.entity.*;
import ma.ensias.mentorpath.user.repository.*;
import ma.ensias.mentorpath.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service de gestion des profils.
 * Gère la lecture et la mise à jour des profils STUDENT et MENTOR.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository           userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final MentorProfileRepository  mentorProfileRepository;

    // ── getProfile ────────────────────────────────────────────────────────────

    @Override
    public UserDTO getProfile(String email) {
        User user = findUserByEmail(email);

        return switch (user.getRole()) {
            case STUDENT -> {
                StudentProfile p = studentProfileRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Profil étudiant", user.getId()));
                yield buildStudentDTO(user, p);
            }
            case MENTOR -> {
                MentorProfile p = mentorProfileRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Profil mentor", user.getId()));
                yield buildMentorDTO(user, p);
            }
            case ADMIN -> buildAdminDTO(user);
        };
    }

    // ── updateProfile ─────────────────────────────────────────────────────────

    @Override
    @Transactional
    public UserDTO updateProfile(String email, UpdateProfileRequest req) {
        User user = findUserByEmail(email);

        return switch (user.getRole()) {
            case STUDENT -> {
                StudentProfile p = studentProfileRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Profil étudiant", user.getId()));
                if (req.getFirstName()  != null) p.setFirstName(req.getFirstName());
                if (req.getLastName()   != null) p.setLastName(req.getLastName());
                if (req.getFiliere()    != null) p.setFiliere(req.getFiliere());
                if (req.getBio()        != null) p.setBio(req.getBio());
                if (req.getAvatarUrl()  != null) p.setAvatarUrl(req.getAvatarUrl());
                if (req.getAnneeEtude() != null) p.setAnneeEtude(req.getAnneeEtude());
                studentProfileRepository.save(p);
                yield buildStudentDTO(user, p);
            }
            case MENTOR -> {
                MentorProfile p = mentorProfileRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Profil mentor", user.getId()));
                if (req.getFirstName() != null) p.setFirstName(req.getFirstName());
                if (req.getLastName()  != null) p.setLastName(req.getLastName());
                if (req.getFiliere()   != null) p.setFiliere(req.getFiliere());
                if (req.getBio()       != null) p.setBio(req.getBio());
                if (req.getAvatarUrl() != null) p.setAvatarUrl(req.getAvatarUrl());
                if (req.getPromo()     != null) p.setPromo(req.getPromo());
                if (req.getExpertise() != null) p.setExpertise(req.getExpertise());
                if (req.getAvailable() != null) p.setAvailable(req.getAvailable());
                mentorProfileRepository.save(p);
                yield buildMentorDTO(user, p);
            }
            case ADMIN -> buildAdminDTO(user);
        };
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + email));
    }

    private UserDTO buildStudentDTO(User user, StudentProfile p) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setFiliere(p.getFiliere());
        dto.setBio(p.getBio());
        dto.setAvatarUrl(p.getAvatarUrl());
        return dto;
    }

    private UserDTO buildMentorDTO(User user, MentorProfile p) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setFiliere(p.getFiliere());
        dto.setBio(p.getBio());
        dto.setAvatarUrl(p.getAvatarUrl());
        return dto;
    }

    private UserDTO buildAdminDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
