package ma.ensias.mentorpath.user.mapper;

import javax.annotation.processing.Generated;
import ma.ensias.mentorpath.user.dto.UserDTO;
import ma.ensias.mentorpath.user.entity.MentorProfile;
import ma.ensias.mentorpath.user.entity.StudentProfile;
import ma.ensias.mentorpath.user.entity.User;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-14T02:11:14+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO studentProfileToDTO(StudentProfile studentProfile) {
        if ( studentProfile == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( studentProfileUserId( studentProfile ) );
        userDTO.setEmail( studentProfileUserEmail( studentProfile ) );
        userDTO.setAvatarUrl( studentProfile.getAvatarUrl() );
        userDTO.setBio( studentProfile.getBio() );
        userDTO.setFiliere( studentProfile.getFiliere() );
        userDTO.setFirstName( studentProfile.getFirstName() );
        userDTO.setLastName( studentProfile.getLastName() );

        userDTO.setRole( studentProfile.getUser().getRole().name() );

        return userDTO;
    }

    @Override
    public UserDTO mentorProfileToDTO(MentorProfile mentorProfile) {
        if ( mentorProfile == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( mentorProfileUserId( mentorProfile ) );
        userDTO.setEmail( mentorProfileUserEmail( mentorProfile ) );
        userDTO.setAvatarUrl( mentorProfile.getAvatarUrl() );
        userDTO.setBio( mentorProfile.getBio() );
        userDTO.setFiliere( mentorProfile.getFiliere() );
        userDTO.setFirstName( mentorProfile.getFirstName() );
        userDTO.setLastName( mentorProfile.getLastName() );

        userDTO.setRole( mentorProfile.getUser().getRole().name() );

        return userDTO;
    }

    private Long studentProfileUserId(StudentProfile studentProfile) {
        if ( studentProfile == null ) {
            return null;
        }
        User user = studentProfile.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String studentProfileUserEmail(StudentProfile studentProfile) {
        if ( studentProfile == null ) {
            return null;
        }
        User user = studentProfile.getUser();
        if ( user == null ) {
            return null;
        }
        String email = user.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private Long mentorProfileUserId(MentorProfile mentorProfile) {
        if ( mentorProfile == null ) {
            return null;
        }
        User user = mentorProfile.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String mentorProfileUserEmail(MentorProfile mentorProfile) {
        if ( mentorProfile == null ) {
            return null;
        }
        User user = mentorProfile.getUser();
        if ( user == null ) {
            return null;
        }
        String email = user.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }
}
