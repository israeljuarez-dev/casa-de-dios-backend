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

    boolean existsByDniAndActiveTrue(String dni);

    boolean existsByPhoneNumberAndActiveTrue(String phoneNumber);

    @Query(DiscipleQueries.EXISTS_BY_DNI_AND_ID_NOT_AND_ACTIVE_TRUE)
    boolean existsByDniAndIdNotAndActiveTrue(@Param("dni") String dni, @Param("id") Long id);

    @Query(DiscipleQueries.EXISTS_BY_PHONE_NUMBER_AND_ID_NOT_AND_ACTIVE_TRUE)
    boolean existsByPhoneNumberAndIdNotAndActiveTrue(@Param("phoneNumber") String phoneNumber, @Param("id") Long id);

    @Query(DiscipleQueries.SOFT_DELETE_BY_ID)
    @Modifying
    void softDeleteById(@Param("id") Long id);
}
