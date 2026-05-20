package ma.ensias.mentorpath.user.service;

import ma.ensias.mentorpath.user.dto.UpdateProfileRequest;
import ma.ensias.mentorpath.user.dto.UserDTO;

/**
 * Interface du service de gestion des profils utilisateurs.
 */
public interface UserService {

    /**
     * Récupère le profil complet de l'utilisateur connecté.
     *
     * @param email email extrait du token JWT
     * @return DTO du profil
     */
    UserDTO getProfile(String email);

    /**
     * Met à jour le profil de l'utilisateur connecté.
     *
     * @param email email extrait du token JWT
     * @param req   champs à modifier
     * @return DTO mis à jour
     */
    UserDTO updateProfile(String email, UpdateProfileRequest req);
}
