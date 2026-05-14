package ma.ensias.mentorpath.user.service;

import ma.ensias.mentorpath.exception.ResourceNotFoundException;
import ma.ensias.mentorpath.user.dto.UpdateProfileRequest;
import ma.ensias.mentorpath.user.dto.UserDTO;
import ma.ensias.mentorpath.user.entity.*;
import ma.ensias.mentorpath.user.repository.*;
import ma.ensias.mentorpath.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl – Tests unitaires")
class UserServiceImplTest {

    @Mock private UserRepository           userRepository;
    @Mock private StudentProfileRepository studentProfileRepository;
    @Mock private MentorProfileRepository  mentorProfileRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // ── getProfile ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getProfile() – doit retourner le profil étudiant correct")
    void getProfile_student_success() {
        User user = User.builder()
                .id(1L).email("student@ensias.ma").role(Role.STUDENT).enabled(true).build();

        StudentProfile profile = StudentProfile.builder()
                .id(1L).user(user)
                .firstName("Fatima").lastName("Zahra")
                .filiere("Génie Logiciel").anneeEtude(2)
                .build();

        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(user));
        when(studentProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));

        UserDTO result = userService.getProfile("student@ensias.ma");

        assertThat(result.getEmail()).isEqualTo("student@ensias.ma");
        assertThat(result.getFirstName()).isEqualTo("Fatima");
        assertThat(result.getRole()).isEqualTo("STUDENT");
    }

    @Test
    @DisplayName("getProfile() – doit lever ResourceNotFoundException si l'utilisateur n'existe pas")
    void getProfile_userNotFound_throwsException() {
        when(userRepository.findByEmail("unknown@ensias.ma")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getProfile("unknown@ensias.ma"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ── updateProfile ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("updateProfile() – doit mettre à jour uniquement les champs non-null")
    void updateProfile_student_partialUpdate() {
        User user = User.builder()
                .id(1L).email("student@ensias.ma").role(Role.STUDENT).enabled(true).build();

        StudentProfile profile = StudentProfile.builder()
                .id(1L).user(user)
                .firstName("Fatima").lastName("Zahra")
                .filiere("Génie Logiciel")
                .build();

        when(userRepository.findByEmail("student@ensias.ma")).thenReturn(Optional.of(user));
        when(studentProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(studentProfileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UpdateProfileRequest req = new UpdateProfileRequest();
        req.setBio("Passionnée de Java et Spring");

        UserDTO result = userService.updateProfile("student@ensias.ma", req);

        // Le firstName original est conservé
        assertThat(result.getFirstName()).isEqualTo("Fatima");
        // La bio a été mise à jour
        assertThat(result.getBio()).isEqualTo("Passionnée de Java et Spring");
        verify(studentProfileRepository).save(any(StudentProfile.class));
    }


    @Test
    @DisplayName("getProfile() – doit retourner le profil mentor correct")
         void getProfile_mentor_success() {
                 User user = User.builder()
            .id(2L).email("mentor@ensias.ma").role(Role.MENTOR).enabled(true).build();

         MentorProfile profile = MentorProfile.builder()
            .id(1L).user(user)
            .firstName("Youssef").lastName("Alami")
            .filiere("Génie Logiciel")
            .build();

        when(userRepository.findByEmail("mentor@ensias.ma")).thenReturn(Optional.of(user));
        when(mentorProfileRepository.findByUserId(2L)).thenReturn(Optional.of(profile));

         UserDTO result = userService.getProfile("mentor@ensias.ma");

         assertThat(result.getRole()).isEqualTo("MENTOR");
         assertThat(result.getFirstName()).isEqualTo("Youssef");
}


@Test
@DisplayName("updateProfile() – doit mettre à jour le profil mentor")
void updateProfile_mentor_success() {
    User user = User.builder()
            .id(2L).email("mentor@ensias.ma").role(Role.MENTOR).enabled(true).build();
    MentorProfile profile = MentorProfile.builder()
            .id(1L).user(user).firstName("Youssef").lastName("Alami").build();

    when(userRepository.findByEmail("mentor@ensias.ma")).thenReturn(Optional.of(user));
    when(mentorProfileRepository.findByUserId(2L)).thenReturn(Optional.of(profile));
    when(mentorProfileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    UpdateProfileRequest req = new UpdateProfileRequest();
    req.setBio("Expert Spring Boot");
    req.setAvailable(true);

    UserDTO result = userService.updateProfile("mentor@ensias.ma", req);

    assertThat(result.getBio()).isEqualTo("Expert Spring Boot");
}

}

