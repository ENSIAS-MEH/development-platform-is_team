package ma.ensias.mentorpath.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lors de l'inscription quand l'email est déjà pris.
 * Correspond au HTTP 409 Conflict.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException(String email) {
        super("L'adresse email est déjà utilisée : " + email);
    }
}
