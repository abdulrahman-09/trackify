package com.mujahid.trackify.security.jwt;

import com.mujahid.trackify.security.Principal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {

    private final String secret;

    private final Long jwtExTime;

    private static final int MINIMUM_KEY_LENGTH = 32;

    private SecretKey signingKey;



    public JwtService(@Value("${JWT_SECRET_KEY}") String secret,
                      @Value("${JWT_EXPIRATION}") Long jwtExTime) {
        this.secret = secret;
        this.jwtExTime = jwtExTime;
    }

    // JWT secret format and validation
    @PostConstruct
    public void validateSecretKey() {
        log.info("Validating JWT secret key format and length...");

        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalStateException("JWT_SECRET_KEY is not set. Cannot initialize JwtService.");
        }

        byte[] keyBytes = decodeSecret(secret);

        if (keyBytes.length < MINIMUM_KEY_LENGTH) {
            throw new IllegalStateException(
                    String.format("JWT secret too short. Current: %d bytes, Required: %d bytes (256 bits minimum). " +
                                    "Generate a new key with: openssl rand -base64 32",
                            keyBytes.length, MINIMUM_KEY_LENGTH)
            );
        }

        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        log.info("JWT secret key validated successfully. Key size: {} bytes", keyBytes.length);
    }

    private byte[] decodeSecret(String secret) {
        // Try Base64 first (recommended format)
        try {
            byte[] decoded = Decoders.BASE64.decode(secret);
            log.debug("JWT secret successfully decoded from Base64");
            return decoded;
        } catch (IllegalArgumentException e) {
            log.debug("Base64 decoding failed, attempting hex format");
        }
        throw new IllegalStateException(
                "JWT secret format is invalid."
        );
    }

    // Token generation
    public String generateToken(Principal principal) {
        return generateToken(Map.of(), principal);
    }

    private String generateToken(Map<String, Object> extraClaims, Principal principal) {
        Long currentInMs = System.currentTimeMillis();
        return Jwts.builder()
                .subject(principal.getUsername())
                .claim("userId", principal.getUserId().toString())
                .claims(extraClaims)
                .issuedAt(new Date(currentInMs))
                .expiration(new Date(currentInMs + jwtExTime))
                .signWith(this.signingKey)
                .compact();
    }

    // Token validation
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(this.signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractEmail(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

}
