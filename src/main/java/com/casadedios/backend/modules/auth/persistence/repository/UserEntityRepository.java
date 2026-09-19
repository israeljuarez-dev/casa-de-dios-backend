package com.casadedios.backend.modules.auth.persistence.repository;

import com.casadedios.backend.modules.auth.persistence.model.UserEntity;
import com.casadedios.backend.common.enums.GenderEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    long countByGender(GenderEnum gender);
}
