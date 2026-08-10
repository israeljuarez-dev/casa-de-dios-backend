package com.casadedios.backend.disciple.persistence.specification;

import com.casadedios.backend.disciple.dto.request.DiscipleSearchCriteriaDto;
import com.casadedios.backend.disciple.persistence.model.Disciple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class DiscipleSpecification {

    private DiscipleSpecification() {}

    public static Specification<Disciple> withSearchCriteria(DiscipleSearchCriteriaDto criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isTrue(root.get("active")));

            if (criteria.search() != null) {
                predicates.add(buildSearchPredicate(criteria.search(), root, criteriaBuilder));
            } else {
                addFirstNamePredicate(criteria.firstName(), predicates, root, criteriaBuilder);
                addLastNamePredicate(criteria.lastName(), predicates, root, criteriaBuilder);
            }

            if (criteria.gender() != null) {
                predicates.add(criteriaBuilder.equal(root.get("gender"), criteria.gender()));
            }

            if (criteria.spiritualLevel() != null) {
                predicates.add(criteriaBuilder.equal(root.get("spiritualLevel"), criteria.spiritualLevel()));
            }

            if (criteria.maritalStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("maritalStatus"), criteria.maritalStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate buildSearchPredicate(String search, Root<Disciple> root, CriteriaBuilder criteriaBuilder) {
        String normalized = search.toLowerCase().trim();

        return normalized.contains(" ")
                ? buildTwoTokenSearchPredicate(normalized, root, criteriaBuilder)
                : buildSingleTokenSearchPredicate(normalized, root, criteriaBuilder);
    }

    private static Predicate buildTwoTokenSearchPredicate(String normalized, Root<Disciple> root, CriteriaBuilder criteriaBuilder) {
        String[] tokens = normalized.split("\\s+", 2);
        String patternA = likePattern(tokens[0]);
        String patternB = likePattern(tokens[1]);

        Predicate firstNameFirst = criteriaBuilder.and(
                ilike(root, criteriaBuilder, "firstName", patternA),
                ilike(root, criteriaBuilder, "lastName", patternB)
        );

        Predicate lastNameFirst = criteriaBuilder.and(
                ilike(root, criteriaBuilder, "firstName", patternB),
                ilike(root, criteriaBuilder, "lastName", patternA)
        );

        return criteriaBuilder.or(firstNameFirst, lastNameFirst);
    }

    private static Predicate buildSingleTokenSearchPredicate(String normalized, Root<Disciple> root, CriteriaBuilder criteriaBuilder) {
        String pattern = likePattern(normalized);

        return criteriaBuilder.or(
                ilike(root, criteriaBuilder, "firstName", pattern),
                ilike(root, criteriaBuilder, "lastName", pattern)
        );
    }

    private static void addFirstNamePredicate(String firstName, List<Predicate> predicates, Root<Disciple> root, CriteriaBuilder criteriaBuilder) {
        if (firstName != null && !firstName.isBlank()) {
            predicates.add(ilike(root, criteriaBuilder, "firstName", likePattern(firstName.toLowerCase())));
        }
    }

    private static void addLastNamePredicate(String lastName, List<Predicate> predicates, Root<Disciple> root, CriteriaBuilder criteriaBuilder) {
        if (lastName != null && !lastName.isBlank()) {
            predicates.add(ilike(root, criteriaBuilder, "lastName", likePattern(lastName.toLowerCase())));
        }
    }

    private static Predicate ilike(Root<Disciple> root, CriteriaBuilder criteriaBuilder, String field, String pattern) {
        return criteriaBuilder.like(
                criteriaBuilder.function("unaccent", String.class,
                        criteriaBuilder.lower(root.get(field))
                ),
                pattern
        );
    }

    private static String likePattern(String value) {
        return "%" + stripAccents(value) + "%";
    }

    private static String stripAccents(String value) {
        return java.text.Normalizer
                .normalize(value, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "");
    }
}
