package com.casadedios.backend.auth.service.impl;

import com.casadedios.backend.auth.dto.request.AuthLoginRequestDto;
import com.casadedios.backend.auth.dto.response.AuthLoginResponseDto;
import com.casadedios.backend.auth.persistence.model.UserEntity;
import com.casadedios.backend.auth.persistence.repository.UserEntityRepository;
import com.casadedios.backend.security.jwt.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Buscando usuario por username o email: '{}'", username);

        UserEntity userEntity = userEntityRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> {
                    log.warn("El usuario no fue encontrado.");
                    return new UsernameNotFoundException("Usuario o contraseña incorrectos");
                });

        List<SimpleGrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name()));

        log.info("Usuario obtenido exitosamente con rol {}", userEntity.getRole());

        return new User(
                userEntity.getUsername(),
                userEntity.getPasswordHash(),
                userEntity.isEnabled(),
                userEntity.isAccountNotExpired(),
                userEntity.isCredentialNotExpired(),
                userEntity.isAccountNotLocked(),
                authorityList
        );
    }

    @Transactional
    public AuthLoginResponseDto login(AuthLoginRequestDto loginRequestDto) {
        String usernameOrEmail = loginRequestDto.usernameOrEmail();
        String password = loginRequestDto.password();

        log.info("Iniciando proceso de login...");

        Authentication authentication = authenticate(usernameOrEmail, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = userEntityRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        user.setLastLoginAt(Instant.now());

        String accessToken = jwtUtils.generateToken(authentication);

        log.info("¡Inicio de sesión exitoso!");

        return AuthLoginResponseDto.builder()
                .usernameOrEmail(usernameOrEmail)
                .jwt(accessToken)
                .build();
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            log.warn("Autenticación fallida: contraseña incorrecta");
            throw new BadCredentialsException("Invalid username or password");
        }

        log.info("¡Autenticación exitosa!");

        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                userDetails.getAuthorities()
        );
    }
}
