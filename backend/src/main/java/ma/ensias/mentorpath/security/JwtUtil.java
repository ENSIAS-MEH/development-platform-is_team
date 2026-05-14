package ma.ensias.mentorpath.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utilitaire JWT : génération, validation et extraction des claims.
 * Utilise HMAC-SHA256 avec une clé dérivée du secret applicatif.
 */
@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expirationMs;

    // ── Génération ───────────────────────────────────────────────────────────

    /**
     * Génère un token JWT signé pour un utilisateur.
     *
     * @param email email de l'utilisateur (subject)
     * @param role  rôle de l'utilisateur (claim)
     * @return token JWT sous forme de String
     */
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ── Extraction ───────────────────────────────────────────────────────────

    /** Extrait l'email (subject) depuis un token. */
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    /** Extrait le rôle depuis un token. */
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // ── Validation ───────────────────────────────────────────────────────────

    /**
     * Vérifie que le token est bien signé et non expiré.
     *
     * @param token le token JWT brut
     * @return true si le token est valide
     */
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ── Privé ─────────────────────────────────────────────────────────────────

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
