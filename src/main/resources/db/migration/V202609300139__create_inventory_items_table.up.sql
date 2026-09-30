-- ============================================================================
-- inventory_items
-- ============================================================================
CREATE TABLE inventory_items (
                                 id                  BIGINT GENERATED ALWAYS AS IDENTITY,

                                 name                VARCHAR(150) NOT NULL,
                                 description         TEXT,
                                 cost                NUMERIC(10, 2),
                                 quantity            INTEGER NOT NULL DEFAULT 1,
                                 category            VARCHAR(100) NOT NULL,

                                 source_type         VARCHAR(10) NOT NULL,
                                 donor_disciple_id   BIGINT,

                                 registered_at       DATE NOT NULL DEFAULT CURRENT_DATE,

                                 created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
                                 updated_at          TIMESTAMPTZ NOT NULL DEFAULT now(),

                                 CONSTRAINT pk_inventory_items PRIMARY KEY (id),
                                 CONSTRAINT fk_inventory_items_donor FOREIGN KEY (donor_disciple_id)
                                     REFERENCES disciples(id),
                                 CONSTRAINT chk_inventory_items_cost CHECK (cost >= 0),
                                 CONSTRAINT chk_inventory_items_quantity CHECK (quantity >= 0),
                                 CONSTRAINT chk_inventory_items_source_type CHECK (source_type IN ('DONATED', 'PURCHASED')),
                                 CONSTRAINT chk_inventory_items_donor CHECK (
                                     (source_type = 'DONATED' AND donor_disciple_id IS NOT NULL)
                                         OR (source_type = 'PURCHASED')
                                     )
);

CREATE INDEX idx_inventory_items_category ON inventory_items (category);
CREATE INDEX idx_inventory_items_source_type ON inventory_items (source_type);
CREATE INDEX idx_inventory_items_registered_at ON inventory_items (registered_at);
CREATE INDEX idx_inventory_items_donor_disciple_id ON inventory_items (donor_disciple_id);

COMMENT ON TABLE inventory_items IS 'Bienes y utensilios de la iglesia (inventario)';
COMMENT ON COLUMN inventory_items.id IS 'Identificador único autogenerado del ítem de inventario';
COMMENT ON COLUMN inventory_items.name IS 'Nombre del utensilio o producto';
COMMENT ON COLUMN inventory_items.description IS 'Descripción del ítem';
COMMENT ON COLUMN inventory_items.cost IS 'Costo unitario del ítem si fue comprado por la iglesia; nulo o irrelevante si fue donado';
COMMENT ON COLUMN inventory_items.quantity IS 'Stock actual: cantidad de unidades de este ítem (ej. 150 sillas); nunca negativo';
COMMENT ON COLUMN inventory_items.category IS 'Categoría o tipo del ítem (cocina, limpieza, salón principal, etc.); texto libre, sin catálogo cerrado, ya que puede crecer según el uso del pastor/pastora';
COMMENT ON COLUMN inventory_items.source_type IS 'Origen del ítem: DONATED (donado por un discípulo) o PURCHASED (comprado por la iglesia)';
COMMENT ON COLUMN inventory_items.donor_disciple_id IS 'Discípulo que donó el ítem; obligatorio cuando source_type es DONATED';
COMMENT ON COLUMN inventory_items.registered_at IS 'Fecha exacta de ingreso del producto al inventario';
COMMENT ON COLUMN inventory_items.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN inventory_items.updated_at IS 'Fecha y hora de la última modificación del registro';