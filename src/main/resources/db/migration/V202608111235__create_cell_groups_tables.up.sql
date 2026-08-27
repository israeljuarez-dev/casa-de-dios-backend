-- ============================================================================
-- cell_groups
-- ============================================================================
CREATE TABLE cell_groups (
                             id                  BIGINT GENERATED ALWAYS AS IDENTITY,

                             name                VARCHAR(150),
                             leader_disciple_id  BIGINT,
                             meeting_day         VARCHAR(15),
                             meeting_time        TIME,
                             location            VARCHAR(255),
                             is_pastor_cell      BOOLEAN NOT NULL DEFAULT FALSE,
                             pastor_cell_gender   VARCHAR(10),
                             created_at          TIMESTAMP NOT NULL DEFAULT now(),
                             updated_at          TIMESTAMP NOT NULL DEFAULT now(),


                             CONSTRAINT pk_cell_groups PRIMARY KEY (id),
                             CONSTRAINT uq_cell_groups_name UNIQUE (name),
                             CONSTRAINT fk_cell_groups_leader FOREIGN KEY (leader_disciple_id) REFERENCES disciples(id),
                             CONSTRAINT chk_cell_groups_meeting_day CHECK (meeting_day IN
                                                                           ('MONDAY','TUESDAY','WEDNESDAY','FRIDAY','SATURDAY')),
                             CONSTRAINT chk_cell_groups_pastor_cell_gender CHECK (pastor_cell_gender IN ('MALE', 'FEMALE'))
);

CREATE INDEX idx_cell_groups_leader_disciple_id ON cell_groups (leader_disciple_id);

COMMENT ON TABLE cell_groups IS 'Célula: reunión semanal en día/hora/lugar fijo, dirigida por un único líder';
COMMENT ON COLUMN cell_groups.id IS 'Identificador único autogenerado de la célula';
COMMENT ON COLUMN cell_groups.name IS 'Nombre de la célula (ej. "Los Bendecidos")';
COMMENT ON COLUMN cell_groups.leader_disciple_id IS 'Discípulo líder de esta célula; un discípulo puede liderar varias células (relación uno a muchos)';
COMMENT ON COLUMN cell_groups.meeting_day IS 'Día de la semana en que se realiza la reunión';
COMMENT ON COLUMN cell_groups.meeting_time IS 'Hora de inicio de la reunión';
COMMENT ON COLUMN cell_groups.location IS 'Lugar donde se realiza la reunión';
COMMENT ON COLUMN cell_groups.is_pastor_cell IS 'Indica si esta es la célula principal del pastor o la pastora';
COMMENT ON COLUMN cell_groups.pastor_cell_gender IS 'Género de los miembros aceptados en la célula principal: MALE para la célula del pastor, FEMALE para la de la pastora. Null para células regulares.';
COMMENT ON COLUMN cell_groups.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN cell_groups.updated_at IS 'Fecha y hora de la última modificación del registro';


-- ============================================================================
-- cell_group_members
-- ============================================================================
CREATE TABLE cell_group_members (
                                    id                      BIGINT GENERATED ALWAYS AS IDENTITY,
                                    cell_group_id           BIGINT NOT NULL,
                                    disciple_id             BIGINT NOT NULL,

                                    is_core_twelve          BOOLEAN NOT NULL DEFAULT FALSE,
                                    is_pastor_core_twelve   BOOLEAN NOT NULL DEFAULT FALSE,

                                    joined_at               DATE NOT NULL DEFAULT CURRENT_DATE,

                                    CONSTRAINT pk_cell_group_members PRIMARY KEY (id),
                                    CONSTRAINT fk_cell_group_members_cell_group FOREIGN KEY (cell_group_id)
                                        REFERENCES cell_groups(id) ON DELETE CASCADE,
                                    CONSTRAINT fk_cell_group_members_disciple FOREIGN KEY (disciple_id)
                                        REFERENCES disciples(id) ON DELETE CASCADE,
                                    CONSTRAINT uq_cell_group_members_cell_disciple UNIQUE (cell_group_id, disciple_id)
);

CREATE INDEX idx_cell_group_members_cell_group_id ON cell_group_members (cell_group_id);
CREATE INDEX idx_cell_group_members_disciple_id ON cell_group_members (disciple_id);
CREATE INDEX idx_cell_group_members_pastor_core_twelve ON cell_group_members (is_pastor_core_twelve)
    WHERE is_pastor_core_twelve = TRUE;

COMMENT ON TABLE cell_group_members IS 'Relación de discípulos asistentes a cada célula, incluyendo la marca de "Los 12"';
COMMENT ON COLUMN cell_group_members.id IS 'Identificador único autogenerado de la membresía';
COMMENT ON COLUMN cell_group_members.cell_group_id IS 'Célula a la que asiste el discípulo';
COMMENT ON COLUMN cell_group_members.disciple_id IS 'Discípulo asistente a la célula';
COMMENT ON COLUMN cell_group_members.is_core_twelve IS 'Indica si el discípulo forma parte de "Los 12" elegidos por el líder de esta célula (subconjunto opcional)';
COMMENT ON COLUMN cell_group_members.is_pastor_core_twelve IS 'Indica si el discípulo forma parte de "Los 12" del pastor o la pastora específicamente (caso particular de is_core_twelve, usado por el módulo "Mis 12")';
COMMENT ON COLUMN cell_group_members.joined_at IS 'Fecha en que el discípulo se integró a la célula';