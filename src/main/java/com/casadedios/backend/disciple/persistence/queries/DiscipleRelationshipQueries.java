package com.casadedios.backend.disciple.persistence.queries;

public final class DiscipleRelationshipQueries {

    private DiscipleRelationshipQueries() {}

    // Solo trae los campos del hijo (target), y el id del padre (source)
    public static final String FIND_CHILDREN_BY_SOURCE_IDS =
            """
            SELECT
                dr.source_disciple_id  AS parentId,
                d.id                   AS childId,
                d.first_name           AS firstName,
                d.last_name            AS lastName,
                d.gender               AS gender,
                d.birth_date           AS birthDate
            FROM disciple_relationships dr
            JOIN disciples d ON d.id = dr.target_disciple_id
            WHERE dr.source_disciple_id IN :sourceIds
              AND dr.relationship_type = :type
            """;

    // Solo trae los campos del invitador (source), y el id del invitado (target)
    public static final String FIND_INVITERS_BY_TARGET_IDS =
            """
            SELECT
                dr.target_disciple_id  AS discipleId,
                d.id                   AS inviterId,
                d.first_name           AS firstName,
                d.last_name            AS lastName
            FROM disciple_relationships dr
            JOIN disciples d ON d.id = dr.source_disciple_id
            WHERE dr.target_disciple_id IN :targetIds
              AND dr.relationship_type = :type
            """;

    public static final String FIND_PARENTS_BY_CHILD_IDS =
            """
            SELECT
                dr.target_disciple_id  AS childId,
                d.id                   AS parentId,
                d.first_name           AS firstName,
                d.last_name            AS lastName,
                d.gender               AS gender
            FROM disciple_relationships dr
            JOIN disciples d ON d.id = dr.source_disciple_id
            WHERE dr.target_disciple_id IN :childIds
              AND dr.relationship_type = :type
            """;

    public static final String FIND_CHILD_IDS_BY_PARENT_ID =
            """
            SELECT
                dr.id               AS relationshipId,
                dr.target_disciple_id AS childId
            FROM disciple_relationships dr
            WHERE dr.source_disciple_id = :parentId
              AND dr.relationship_type = :type
            """;
}
