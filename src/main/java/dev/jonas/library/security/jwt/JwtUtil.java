package dev.jonas.library.security.jwt;

import dev.jonas.library.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * Utility class for generating and validating JWT tokens.
 */
@Component
public class JwtUtil {

    // Secret key for signing tokens (HS256)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token validity duration (1 hour)
    private final long expirationTime = 1000 * 60 * 60;

    /**
     * Generates a JWT token for the specified username.
     */
    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    /**
     * Extracts the username (subject) from the token.
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Validates the token by checking expiration.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }


    /**
     * Parses claims from the token.
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}