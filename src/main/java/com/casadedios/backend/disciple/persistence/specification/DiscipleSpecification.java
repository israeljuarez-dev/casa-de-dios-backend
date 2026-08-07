package com.casadedios.backend.disciple.persistence.specification;

import com.casadedios.backend.disciple.dto.request.DiscipleSearchCriteriaDto;
import com.casadedios.backend.disciple.persistence.model.Disciple;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class DiscipleSpecification {

    private DiscipleSpecification() {}

    public static Specification<Disciple> withSearchCriteria(DiscipleSearchCriteriaDto criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isTrue(root.get("active")));

            // Filtro por nombre
            if (criteria.firstName() != null && !criteria.firstName().isBlank()) {
                String pattern = "%" + criteria.firstName().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), pattern));
            }

            // Filtro por apellido
            if (criteria.lastName() != null && !criteria.lastName().isBlank()) {
                String pattern = "%" + criteria.lastName().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), pattern));
            }

            if (criteria.gender() != null) {
                predicates.add(criteriaBuilder.equal(root.get("gender"), criteria.gender()));
            }

            // Filtro por nivel espiritual
            if (criteria.spiritualLevel() != null) {
                predicates.add(criteriaBuilder.equal(root.get("spiritualLevel"), criteria.spiritualLevel()));
            }

            // Filtro por estado civil
            if (criteria.maritalStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("maritalStatus"), criteria.maritalStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
