package com.casadedios.backend.disciple.persistence.queries;

public final class DiscipleQueries {

    private DiscipleQueries() {}

    public static final String SOFT_DELETE_BY_ID = """
            UPDATE Disciple d
            SET d.active = false
            WHERE d.id = :id
            """;

    public static final String EXISTS_BY_DNI_AND_ID_NOT_AND_ACTIVE_TRUE = """
            SELECT COUNT(d) > 0 FROM Disciple d
            WHERE d.dni = :dni
            AND d.id <> :id
            AND d.active = true
            """;

    public static final String EXISTS_BY_PHONE_NUMBER_AND_ID_NOT_AND_ACTIVE_TRUE = """
            SELECT COUNT(d) > 0 FROM Disciple d
            WHERE d.phoneNumber = :phoneNumber
            AND d.id <> :id
            AND d.active = true
            """;
}
