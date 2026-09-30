package com.casadedios.backend.modules.inventory.persistence.queries;

public final class InventoryItemQueries {

    private InventoryItemQueries() {}

    private static final String SELECT_WITH_DONOR = """
        SELECT
            ii.id               AS id,
            ii.name             AS name,
            ii.description      AS description,
            ii.cost             AS cost,
            ii.quantity         AS quantity,
            ii.category         AS category,
            ii.source_type      AS sourceType,
            d.id                AS donorId,
            d.first_name        AS donorFirstName,
            d.last_name         AS donorLastName,
            ii.registered_at    AS registeredAt
        """;

    private static final String FROM_WITH_DONOR = """
        FROM inventory_items ii
        LEFT JOIN disciples d ON d.id = ii.donor_disciple_id AND d.active = true
        """;

    private static final String SEARCH_FILTERS = """
        WHERE (:name IS NULL OR unaccent(ii.name) ILIKE unaccent(CONCAT('%', :name, '%')))
          AND (:category IS NULL OR unaccent(ii.category) ILIKE unaccent(CONCAT('%', :category, '%')))
          AND (:sourceType IS NULL OR ii.source_type = :sourceType)
          AND (:donorName IS NULL OR
               unaccent(CONCAT(d.first_name, ' ', d.last_name)) ILIKE unaccent(CONCAT('%', :donorName, '%')))
          AND (:registeredFrom IS NULL OR ii.registered_at >= :registeredFrom)
          AND (:registeredTo IS NULL OR ii.registered_at <= :registeredTo)
        """;

    private static final String COUNT_SELECT = """
        SELECT COUNT(*)
        """;

    private static final String BY_ID_FILTER = """
        WHERE ii.id = :id
        """;

    public static final String FIND_ALL_WITH_DONOR = SELECT_WITH_DONOR + FROM_WITH_DONOR + SEARCH_FILTERS;

    public static final String COUNT_ALL_WITH_DONOR = COUNT_SELECT + FROM_WITH_DONOR + SEARCH_FILTERS;

    public static final String FIND_BY_ID_WITH_DONOR = SELECT_WITH_DONOR + FROM_WITH_DONOR + BY_ID_FILTER;

    public static final String ADJUST_STOCK = """
        UPDATE InventoryItem ii
        SET ii.quantity = ii.quantity + :delta,
            ii.updatedAt = :updatedAt
        WHERE ii.id = :id
          AND ii.quantity + :delta >= 0
        """;
}
