package com.casadedios.backend.security.jwt.utils;

import com.casadedios.backend.security.jwt.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;

    public String generateToken(Authentication authentication) {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtProperties.privateKey().getBytes(StandardCharsets.UTF_8));

        String username = Objects.requireNonNull(authentication.getPrincipal()).toString();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtId = UUID.randomUUID().toString();
        Instant now = Instant.now();

        log.info("Generando token JWT (jti={}), expira en {} minutos",jwtId, jwtProperties.expirationMinutes());

        String token = Jwts.builder()
                .issuer(jwtProperties.userGenerator())
                .subject(username)
                .claim("authorities", authorities)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(jwtProperties.expirationMinutes(), ChronoUnit.MINUTES)))
                .id(jwtId)
                .notBefore(Date.from(now))
                .signWith(secretKey)
                .compact();

        log.info("Token JWT generado exitosamente (jti={})", jwtId);
        return token;
    }

    public Claims validateToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.privateKey().getBytes(StandardCharsets.UTF_8));

        try {
            Claims claims = Jwts.parser()
                    .requireIssuer(jwtProperties.userGenerator())
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            log.debug("Token JWT validado exitosamente (jti={})", claims.getId());

            return claims;
        } catch (ExpiredJwtException exception) {
            log.warn("Token JWT expirado");
            throw exception;
        } catch (JwtException exception) {
            log.warn("Token JWT inválido: {}", exception.getMessage());
            throw exception;
        }
    }

    public String extractUsername(Claims claims) {
        return claims.getSubject();
    }

    public Object getSpecificClaim(Claims claims, String claimName) {
        return claims.get(claimName);
    }

    public Map<String, Object> getAllClaims(Claims claims) {
        return claims;
    }
}