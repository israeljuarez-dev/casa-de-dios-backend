package com.casadedios.backend.disciple.persistence.repository;

import com.casadedios.backend.disciple.persistence.model.Disciple;
import com.casadedios.backend.disciple.persistence.queries.DiscipleQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscipleRepository extends JpaRepository<Disciple, Long>, JpaSpecificationExecutor<Disciple> {

    Optional<Disciple> findByIdAndActiveTrue(Long id);

    boolean existsByDni(String dni);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByDniAndIdNot(String dni, Long id);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    @Query(DiscipleQueries.SOFT_DELETE_BY_ID)
    @Modifying
    void softDeleteById(@Param("id") Long id);
}
