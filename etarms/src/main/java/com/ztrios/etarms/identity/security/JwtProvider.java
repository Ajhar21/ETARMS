package com.ztrios.etarms.identity.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final long expirationMs;
    private final String issuer;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs,
            @Value("${jwt.issuer}") String issuer
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
        this.issuer = issuer;
    }

    /* =========================
       TOKEN GENERATION
       ========================= */
    public String generateToken(String username, String role) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .issuer(issuer)
                .issuedAt(now)
                .expiration(expiry)
                .claim("role", role)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /* =========================
       TOKEN VALIDATION
       ========================= */
    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    /* =========================
       CLAIM EXTRACTION
       ========================= */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String getRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
}
