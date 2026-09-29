package com.casadedios.backend.modules.retreat.persistence.queries;

public final class RetreatQueries {

    private RetreatQueries() {}

    public static final String FIND_ALL_WITH_COUNTS = """
        SELECT
            r.id            AS id,
            r.name          AS name,
            r.location      AS location,
            r.start_date    AS startDate,
            r.end_date      AS endDate,
            COUNT(DISTINCT re.id) AS enrolledCount,
            COUNT(DISTINCT rs.id) AS staffCount
        FROM retreats r
        LEFT JOIN retreat_enrollments re ON re.retreat_id = r.id
        LEFT JOIN retreat_staff rs ON rs.retreat_id = r.id
        WHERE (:name IS NULL OR unaccent(r.name) ILIKE unaccent(CONCAT('%', :name, '%')))
          AND (:location IS NULL OR unaccent(r.location) ILIKE unaccent(CONCAT('%', :location, '%')))
          AND (:startDateFrom IS NULL OR r.start_date >= :startDateFrom)
          AND (:startDateTo IS NULL OR r.start_date <= :startDateTo)
        GROUP BY r.id, r.name, r.location, r.start_date, r.end_date
        """;

    public static final String COUNT_ALL = """
        SELECT COUNT(DISTINCT r.id)
        FROM retreats r
        WHERE (:name IS NULL OR unaccent(r.name) ILIKE unaccent(CONCAT('%', :name, '%')))
          AND (:location IS NULL OR unaccent(r.location) ILIKE unaccent(CONCAT('%', :location, '%')))
          AND (:startDateFrom IS NULL OR r.start_date >= :startDateFrom)
          AND (:startDateTo IS NULL OR r.start_date <= :startDateTo)
        """;

    public static final String FIND_BY_ID_WITH_COUNTS = """
        SELECT
            r.id            AS id,
            r.name          AS name,
            r.location      AS location,
            r.start_date    AS startDate,
            r.end_date      AS endDate,
            COUNT(DISTINCT re.id) AS enrolledCount,
            COUNT(DISTINCT rs.id) AS staffCount
        FROM retreats r
        LEFT JOIN retreat_enrollments re ON re.retreat_id = r.id
        LEFT JOIN retreat_staff rs ON rs.retreat_id = r.id
        WHERE r.id = :id
        GROUP BY r.id, r.name, r.location, r.start_date, r.end_date
        """;

    public static final String FIND_ENROLLED_DISCIPLES = """
        SELECT
            re.id                   AS enrollmentId,
            d.id                    AS discipleId,
            d.first_name            AS firstName,
            d.last_name             AS lastName,
            d.phone_code_number     AS phoneCodeNumber,
            d.phone_number          AS phoneNumber,
            re.payment_status       AS paymentStatus,
            re.amount_paid          AS amountPaid
        FROM retreat_enrollments re
        INNER JOIN disciples d ON d.id = re.disciple_id AND d.active = true
        WHERE re.retreat_id = :retreatId
          AND (:search IS NULL OR
               unaccent(d.first_name) ILIKE unaccent(CONCAT('%', :search, '%')) OR
               unaccent(d.last_name) ILIKE unaccent(CONCAT('%', :search, '%')) OR
               unaccent(CONCAT(d.first_name, ' ', d.last_name)) ILIKE unaccent(CONCAT('%', :search, '%')))
          AND (:paymentStatus IS NULL OR re.payment_status = :paymentStatus)
        ORDER BY d.last_name, d.first_name
        """;

    public static final String FIND_STAFF_BY_RETREAT_ID = """
        SELECT
            rs.id                   AS id,
            d.id                    AS discipleId,
            d.first_name            AS firstName,
            d.last_name             AS lastName,
            d.phone_code_number     AS phoneCodeNumber,
            d.phone_number          AS phoneNumber
        FROM retreat_staff rs
        INNER JOIN disciples d ON d.id = rs.disciple_id AND d.active = true
        WHERE rs.retreat_id = :retreatId
        ORDER BY d.last_name, d.first_name
        """;
}