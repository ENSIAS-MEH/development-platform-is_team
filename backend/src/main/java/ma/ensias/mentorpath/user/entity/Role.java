package ma.ensias.mentorpath.user.entity;

/**
 * Rôles possibles dans l'application.
 * Utilisé à la fois dans l'entité User et dans Spring Security.
 */
public enum Role {
    STUDENT,
    MENTOR,
    ADMIN
}
