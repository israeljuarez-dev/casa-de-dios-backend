package com.casadedios.backend.disciple.persistence.queries;

public final class DiscipleQueries {

    private DiscipleQueries() {}

    public static final String SOFT_DELETE_BY_ID = """
            UPDATE Disciple d
            SET d.active = false
            WHERE d.id = :id
            """;
}
