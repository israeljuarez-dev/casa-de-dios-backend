package com.casadedios.backend.cellgroup.persistence.queries;

public final class CellGroupQueries {

    private CellGroupQueries() {}

    public static final String FIND_ALL_WITH_LEADER_AND_COUNT = """
        SELECT
            cg.id                   AS id,
            cg.name                 AS name,
            d.id                    AS leaderId,
            d.first_name            AS leaderFirstName,
            d.last_name             AS leaderLastName,
            cg.meeting_day          AS meetingDay,
            cg.meeting_time         AS meetingTime,
            cg.location             AS location,
            cg.is_pastor_cell       AS isPastorCell,
            cg.pastor_cell_gender   AS pastorCellGender,
            COUNT(cgm.id)           AS memberCount
        FROM cell_groups cg
        LEFT JOIN disciples d ON d.id = cg.leader_disciple_id AND d.active = true
        LEFT JOIN cell_group_members cgm ON cgm.cell_group_id = cg.id
        WHERE (:name IS NULL OR unaccent(cg.name) ILIKE unaccent(CONCAT('%', :name, '%')))
          AND (:leaderName IS NULL OR
               unaccent(CONCAT(d.first_name, ' ', d.last_name)) ILIKE unaccent(CONCAT('%', :leaderName, '%')))
          AND (:meetingDay IS NULL OR cg.meeting_day = :meetingDay)
          AND (:isPastorCell IS NULL OR cg.is_pastor_cell = :isPastorCell)
        GROUP BY cg.id, cg.name, d.id, d.first_name, d.last_name,
                 cg.meeting_day, cg.meeting_time, cg.location, cg.is_pastor_cell, cg.pastor_cell_gender
        """;

    public static final String COUNT_ALL_WITH_LEADER = """
        SELECT COUNT(DISTINCT cg.id)
        FROM cell_groups cg
        LEFT JOIN disciples d ON d.id = cg.leader_disciple_id AND d.active = true
        WHERE (:name IS NULL OR unaccent(cg.name) ILIKE unaccent(CONCAT('%', :name, '%')))
          AND (:leaderName IS NULL OR
               unaccent(CONCAT(d.first_name, ' ', d.last_name)) ILIKE unaccent(CONCAT('%', :leaderName, '%')))
          AND (:meetingDay IS NULL OR cg.meeting_day = :meetingDay)
          AND (:isPastorCell IS NULL OR cg.is_pastor_cell = :isPastorCell)
        """;

    public static final String FIND_MEMBERS_BY_CELL_GROUP_ID = """
            SELECT
                cgm.id                      AS memberId,
                d.id                        AS discipleId,
                d.first_name                AS firstName,
                d.last_name                 AS lastName,
                d.phone_code_number         AS phoneCodeNumber,
                d.phone_number              AS phoneNumber,
                d.spiritual_level           AS spiritualLevel,
                d.birth_date                AS birthDate,
                d.gender                    AS gender,
                d.is_cell_group_leader      AS isCellGroupLeader,
                cgm.is_core_twelve          AS isCoreTwelve,
                cgm.is_pastor_core_twelve   AS isPastorCoreTwelve
            FROM cell_group_members cgm
            INNER JOIN disciples d ON d.id = cgm.disciple_id AND d.active = true
            WHERE cgm.cell_group_id = :cellGroupId
              AND (:search IS NULL OR
                   unaccent(d.first_name) ILIKE unaccent(CONCAT('%', :search, '%')) OR
                   unaccent(d.last_name) ILIKE unaccent(CONCAT('%', :search, '%')) OR
                   unaccent(CONCAT(d.first_name, ' ', d.last_name)) ILIKE unaccent(CONCAT('%', :search, '%')))
              AND (:spiritualLevel IS NULL OR d.spiritual_level = :spiritualLevel)
              AND (:gender IS NULL OR d.gender = :gender)
            ORDER BY d.last_name, d.first_name
            """;

    public static final String FIND_PASTOR_CORE_TWELVE = """
            SELECT
                cgm.id                      AS memberId,
                d.id                        AS discipleId,
                d.first_name                AS firstName,
                d.last_name                 AS lastName,
                d.phone_code_number         AS phoneCodeNumber,
                d.phone_number              AS phoneNumber,
                d.spiritual_level           AS spiritualLevel,
                d.birth_date                AS birthDate,
                d.gender                    AS gender,
                d.is_cell_group_leader      AS isCellGroupLeader,
                cgm.is_core_twelve          AS isCoreTwelve,
                cgm.is_pastor_core_twelve   AS isPastorCoreTwelve
            FROM cell_group_members cgm
            INNER JOIN disciples d ON d.id = cgm.disciple_id AND d.active = true
            WHERE cgm.is_pastor_core_twelve = true
            ORDER BY d.last_name, d.first_name
            """;

    public static final String FIND_DISCIPLE_IDS_BY_DISCIPLE_ID_IN =
            "SELECT cgm.disciple.id FROM CellGroupMember cgm WHERE cgm.disciple.id IN :ids";
}
