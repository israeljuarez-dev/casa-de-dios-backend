package com.casadedios.backend.security.jwt.filter;

import com.casadedios.backend.security.jwt.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    public static final String AUTH_ERROR_ATTRIBUTE = "AUTH_ERROR";

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            log.info("Token recibido, procesando...");
            String jwtToken = authHeader.substring(BEARER_PREFIX.length());

            try {
                Claims claims = jwtUtils.validateToken(jwtToken);

                String username = jwtUtils.extractUsername(claims);
                String authorities = jwtUtils.getSpecificClaim(claims, "authorities").toString();

                Collection<GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                SecurityContext context = SecurityContextHolder.getContext();
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authoritiesList);
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

            } catch (ExpiredJwtException _) {
                request.setAttribute(AUTH_ERROR_ATTRIBUTE, "EXPIRED");
                log.warn("Token JWT expirado en la request a {}", request.getRequestURI());

            } catch (JwtException | IllegalArgumentException exception) {
                request.setAttribute(AUTH_ERROR_ATTRIBUTE, "INVALID");
                log.warn("Token JWT inválido en la request a {}: {}", request.getRequestURI(), exception.getMessage());
            }
        } else if (authHeader != null) {
            log.warn("Esquema de autorización no soportado en la request a {}", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }
}
