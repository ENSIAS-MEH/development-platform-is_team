package ma.ensias.mentorpath.auth.service;

import ma.ensias.mentorpath.auth.dto.JwtResponse;
import ma.ensias.mentorpath.auth.dto.LoginRequest;
import ma.ensias.mentorpath.auth.dto.RegisterRequest;
import ma.ensias.mentorpath.auth.service.impl.AuthServiceImpl;
import ma.ensias.mentorpath.exception.EmailAlreadyUsedException;
import ma.ensias.mentorpath.security.JwtUtil;
import ma.ensias.mentorpath.user.entity.Role;
import ma.ensias.mentorpath.user.entity.User;
import ma.ensias.mentorpath.user.repository.MentorProfileRepository;
import ma.ensias.mentorpath.user.repository.StudentProfileRepository;
import ma.ensias.mentorpath.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour AuthServiceImpl.
 *
 * @ExtendWith(MockitoExtension.class) → active Mockito sans Spring Context (rapide)
 * @Mock → crée un faux objet (ne touche pas la base de données)
 * @InjectMocks → crée l'objet réel en injectant les mocks
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl – Tests unitaires")
class AuthServiceImplTest {

    @Mock private UserRepository           userRepository;
    @Mock private StudentProfileRepository studentProfileRepository;
    @Mock private MentorProfileRepository  mentorProfileRepository;
    @Mock private PasswordEncoder          passwordEncoder;
    @Mock private JwtUtil                  jwtUtil;
    @Mock private AuthenticationManager   authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest studentRequest;
    private RegisterRequest mentorRequest;

    @BeforeEach
    void setUp() {
        studentRequest = new RegisterRequest();
        studentRequest.setEmail("student@ensias.ma");
        studentRequest.setPassword("Password123!");
        studentRequest.setRole(Role.STUDENT);
        studentRequest.setFirstName("Fatima");
        studentRequest.setLastName("Zahra");
        studentRequest.setFiliere("Génie Logiciel");
        studentRequest.setAnneeEtude(2);

        mentorRequest = new RegisterRequest();
        mentorRequest.setEmail("mentor@ensias.ma");
        mentorRequest.setPassword("Password123!");
        mentorRequest.setRole(Role.MENTOR);
        mentorRequest.setFirstName("Youssef");
        mentorRequest.setLastName("Alami");
        mentorRequest.setFiliere("Génie Logiciel");
        mentorRequest.setPromo(2022);
    }

    // ── Register ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("register() – doit retourner un JWT quand l'email est disponible (STUDENT)")
    void register_student_success() {
        when(userRepository.existsByEmail(studentRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u = User.builder()
                    .id(1L)
                    .email(u.getEmail())
                    .password(u.getPassword())
                    .role(u.getRole())
                    .enabled(true)
                    .build();
            return u;
        });
        when(jwtUtil.generateToken(any(), any())).thenReturn("fake.jwt.token");

        JwtResponse result = authService.register(studentRequest);

        assertThat(result.getToken()).isEqualTo("fake.jwt.token");
        assertThat(result.getEmail()).isEqualTo("student@ensias.ma");
        assertThat(result.getRole()).isEqualTo("STUDENT");

        verify(userRepository).save(any(User.class));
        verify(studentProfileRepository).save(any());
        verify(mentorProfileRepository, never()).save(any());
    }

    @Test
    @DisplayName("register() – doit créer un profil MENTOR quand le rôle est MENTOR")
    void register_mentor_success() {
        when(userRepository.existsByEmail(mentorRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtUtil.generateToken(any(), any())).thenReturn("fake.jwt.token");

        authService.register(mentorRequest);

        verify(mentorProfileRepository).save(any());
        verify(studentProfileRepository, never()).save(any());
    }

    @Test
    @DisplayName("register() – doit lever EmailAlreadyUsedException si l'email est pris")
    void register_emailAlreadyTaken_throwsException() {
        when(userRepository.existsByEmail(studentRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(studentRequest))
                .isInstanceOf(EmailAlreadyUsedException.class)
                .hasMessageContaining("student@ensias.ma");

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    @DisplayName("register() – le mot de passe doit être hashé, jamais stocké en clair")
    void register_passwordMustBeHashed() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$hashedValue");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(jwtUtil.generateToken(any(), any())).thenReturn("token");

        authService.register(studentRequest);

        verify(passwordEncoder).encode("Password123!");
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("login() – doit retourner un JWT avec les bons identifiants")
    void login_success() {
        User user = User.builder()
                .id(1L)
                .email("student@ensias.ma")
                .password("hashedPassword")
                .role(Role.STUDENT)
                .enabled(true)
                .build();

        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("student@ensias.ma", "STUDENT")).thenReturn("valid.jwt.token");

        LoginRequest req = new LoginRequest();
        req.setEmail("student@ensias.ma");
        req.setPassword("Password123!");

        JwtResponse result = authService.login(req);

        assertThat(result.getToken()).isEqualTo("valid.jwt.token");
        assertThat(result.getEmail()).isEqualTo("student@ensias.ma");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("login() – doit lever BadCredentialsException avec de mauvais identifiants")
    void login_badCredentials_throwsException() {
        LoginRequest req = new LoginRequest();
        req.setEmail("student@ensias.ma");
        req.setPassword("WrongPassword");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(BadCredentialsException.class);

        verify(userRepository, never()).findByEmail(any());
    }
}
