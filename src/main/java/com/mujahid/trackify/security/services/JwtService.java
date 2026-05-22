package com.mujahid.trackify.security.services;

import com.mujahid.trackify.security.Principal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final String secret;

    private final Long jwtExTime;


    public JwtService(@Value("${JWT_SECRET_KEY}") String secret,
                      @Value("${JWT_EXPIRATION}") Long jwtExTime) {
        this.secret = secret;
        this.jwtExTime = jwtExTime;
    }

    public String generateToken(Principal principal) {
        return generateToken(Map.of(), principal);
    }

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(Map<String, Object> extraClaims, Principal principal) {
        Long currentInMs = System.currentTimeMillis();
        return Jwts.builder()
                .subject(principal.getUsername())
                .claim("userId", principal.getUserId().toString())
                .claims(extraClaims)
                .issuedAt(new Date(currentInMs))
                .expiration(new Date(currentInMs + jwtExTime))
                .signWith(getSigningKey())
                .compact();

    }
}
