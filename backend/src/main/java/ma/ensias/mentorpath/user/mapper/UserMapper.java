package ma.ensias.mentorpath.user.mapper;

import ma.ensias.mentorpath.user.dto.UserDTO;
import ma.ensias.mentorpath.user.entity.MentorProfile;
import ma.ensias.mentorpath.user.entity.StudentProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct pour convertir les entités User/Profile en DTOs.
 * MapStruct génère l'implémentation à la compilation → zéro code boilerplate.
 *
 * componentModel = "spring" → le mapper est un bean Spring injectable.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Convertit un StudentProfile (+ son User) en UserDTO.
     * @Mapping indique à MapStruct comment mapper les champs de sources différentes.
     */
    @Mapping(source = "user.id",    target = "id")
    @Mapping(source = "user.email", target = "email")
    @Mapping(expression = "java(studentProfile.getUser().getRole().name())", target = "role")
    UserDTO studentProfileToDTO(StudentProfile studentProfile);

    /**
     * Convertit un MentorProfile (+ son User) en UserDTO.
     */
    @Mapping(source = "user.id",    target = "id")
    @Mapping(source = "user.email", target = "email")
    @Mapping(expression = "java(mentorProfile.getUser().getRole().name())", target = "role")
    UserDTO mentorProfileToDTO(MentorProfile mentorProfile);
}
