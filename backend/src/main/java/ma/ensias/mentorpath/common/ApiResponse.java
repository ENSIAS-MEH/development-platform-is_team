package ma.ensias.mentorpath.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Enveloppe générique pour toutes les réponses API.
 *
 * Structure retournée au client :
 * {
 *   "success": true,
 *   "message": "Opération réussie",
 *   "data": { ... }
 * }
 *
 * @param <T> type de la donnée retournée (peut être null)
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String  message;
    private T       data;

    /** Réponse de succès avec données. */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /** Réponse de succès sans données. */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    /** Réponse d'erreur. */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
