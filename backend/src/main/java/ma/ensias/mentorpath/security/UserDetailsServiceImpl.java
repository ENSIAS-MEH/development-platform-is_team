package ma.ensias.mentorpath.security;

import lombok.RequiredArgsConstructor;
import ma.ensias.mentorpath.user.entity.User;
import ma.ensias.mentorpath.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation de UserDetailsService utilisée par Spring Security
 * pour charger un utilisateur depuis la base de données lors de l'authentification.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Charge un utilisateur par son email.
     * Spring Security appelle cette méthode automatiquement lors d'une tentative de connexion.
     *
     * @param email l'identifiant de l'utilisateur
     * @return un UserDetails conforme à Spring Security
     * @throws UsernameNotFoundException si aucun utilisateur n'a cet email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur introuvable avec l'email : " + email));

        // Le rôle est préfixé ROLE_ pour que @PreAuthorize("hasRole('STUDENT')") fonctionne
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getEnabled(),
                true, true, true,
                List.of(authority)
        );
    }
}
