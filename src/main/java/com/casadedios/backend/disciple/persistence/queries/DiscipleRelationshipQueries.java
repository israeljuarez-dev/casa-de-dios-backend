package com.casadedios.backend.disciple.persistence.queries;

public final class DiscipleRelationshipQueries {

    private DiscipleRelationshipQueries() {}

    public static final String FIND_CHILDREN_BY_SOURCE_IDS_AND_TYPE = """
            SELECT dr FROM DiscipleRelationship dr
            JOIN FETCH dr.sourceDisciple
            JOIN FETCH dr.targetDisciple
            WHERE dr.sourceDisciple.id IN :sourceIds
            AND dr.relationshipType = :type
            """;

    public static final String FIND_INVITERS_BY_TARGET_IDS_AND_TYPE = """
            SELECT dr FROM DiscipleRelationship dr
            JOIN FETCH dr.sourceDisciple
            WHERE dr.targetDisciple.id IN :targetIds
            AND dr.relationshipType = :type
            """;
}
