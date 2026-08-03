-- ============================================================================
-- disciples
-- ============================================================================
CREATE TABLE disciples (
                           id                  BIGINT GENERATED ALWAYS AS IDENTITY,

                           first_name          VARCHAR(150) NOT NULL,
                           last_name           VARCHAR(150) NOT NULL,
                           birth_date          DATE NOT NULL,
                           occupation          VARCHAR(150),

                           phone_code_number       VARCHAR(10),
                           phone_number        VARCHAR(20),
                           address             VARCHAR(255),
                           dni                 VARCHAR(20),

                           marital_status      VARCHAR(20) NOT NULL DEFAULT 'SINGLE',
                           couple_name         VARCHAR(150),

                           spiritual_level     VARCHAR(30) NOT NULL DEFAULT 'GUEST',
                           is_leader           BOOLEAN NOT NULL DEFAULT FALSE,
                           active              BOOLEAN NOT NULL DEFAULT TRUE,

                           created_at          TIMESTAMP NOT NULL DEFAULT now(),
                           updated_at          TIMESTAMP NOT NULL DEFAULT now(),

                           CONSTRAINT pk_disciples PRIMARY KEY (id),
                           CONSTRAINT uq_disciples_dni UNIQUE (dni),
                           CONSTRAINT uq_disciples_phone_number UNIQUE (phone_number),
                           CONSTRAINT chk_disciples_marital_status CHECK (marital_status IN
                                                                          ('MARRIED', 'DIVORCED', 'WIDOWED', 'SINGLE', 'COHABITING')),
                           CONSTRAINT chk_disciples_spiritual_level CHECK (spiritual_level IN (
                                                                                               'GUEST', 'PRE_RETREAT', 'RETREAT', 'POST_RETREAT',
                                                                                               'LEADERSHIP_SCHOOL_1', 'LEADERSHIP_SCHOOL_2', 'LEADERSHIP_SCHOOL_3',
                                                                                               'LEADERSHIP_SCHOOL_4', 'LEADERSHIP_SCHOOL_5', 'LEADERSHIP_SCHOOL_6',
                                                                                               'LEADER', 'CELL_LEADER', 'LEADERSHIP_SCHOOL_TEACHER'
                               ))
);

CREATE INDEX idx_disciples_last_first_name ON disciples (last_name, first_name);
CREATE INDEX idx_disciples_spiritual_level ON disciples (spiritual_level);
CREATE INDEX idx_disciples_marital_status ON disciples (marital_status);
CREATE INDEX idx_disciples_birth_date ON disciples (birth_date);
CREATE INDEX idx_disciples_active ON disciples (active);
CREATE INDEX idx_disciples_active_true ON disciples (active) WHERE active = TRUE;

COMMENT ON TABLE disciples IS 'Entidad central del sistema: toda persona que asiste a la iglesia';
COMMENT ON COLUMN disciples.id IS 'Identificador único autogenerado del discípulo';
COMMENT ON COLUMN disciples.first_name IS 'Nombres del discípulo';
COMMENT ON COLUMN disciples.last_name IS 'Apellidos del discípulo';
COMMENT ON COLUMN disciples.birth_date IS 'Fecha de nacimiento; a partir de este valor la aplicación calcula la edad actual y los días restantes para el próximo cumpleaños';
COMMENT ON COLUMN disciples.occupation IS 'Profesión u oficio del discípulo (opcional)';
COMMENT ON COLUMN disciples.phone_code_number IS 'Código de país o prefijo telefónico del discípulo (ej: +51, +1), usado por el frontend para construir el enlace wa.me';
COMMENT ON COLUMN disciples.phone_number IS 'Número de celular. Se usa para generar el enlace directo a WhatsApp (wa.me) en el frontend';
COMMENT ON COLUMN disciples.address IS 'Dirección domiciliaria';
COMMENT ON COLUMN disciples.dni IS 'Documento de identidad';
COMMENT ON COLUMN disciples.marital_status IS 'Estado civil; por defecto SINGLE (aplica también a menores donde no corresponde otro estado)';
COMMENT ON COLUMN disciples.couple_name IS 'Nombre del cónyuge; aplica únicamente cuando marital_status es distinto de SINGLE (regla validada en capa de servicio)';
COMMENT ON COLUMN disciples.spiritual_level IS 'Nivel espiritual/formativo actual del discípulo dentro del proceso de la iglesia';
COMMENT ON COLUMN disciples.is_leader IS 'Indica si el discípulo completó el nivel 6 de la Escuela de Líderes y está habilitado para dirigir una célula';
COMMENT ON COLUMN disciples.active IS 'Indica si el discípulo está activo; FALSE = borrado lógico (soft delete), no visible en consultas normales';
COMMENT ON COLUMN disciples.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN disciples.updated_at IS 'Fecha y hora de la última modificación del registro';

-- ============================================================================
-- disciple_relationships
-- ============================================================================
CREATE TABLE disciple_relationships (
                                        id                    BIGINT GENERATED ALWAYS AS IDENTITY,
                                        source_disciple_id    BIGINT NOT NULL,
                                        target_disciple_id    BIGINT NOT NULL,
                                        relationship_type     VARCHAR(20) NOT NULL,

                                        created_at            TIMESTAMP NOT NULL DEFAULT now(),

                                        CONSTRAINT pk_disciple_relationships PRIMARY KEY (id),
                                        CONSTRAINT fk_disciple_relationships_source FOREIGN KEY (source_disciple_id)
                                            REFERENCES disciples(id) ON DELETE CASCADE,
                                        CONSTRAINT fk_disciple_relationships_target FOREIGN KEY (target_disciple_id)
                                            REFERENCES disciples(id) ON DELETE CASCADE,
                                        CONSTRAINT uq_disciple_relationships UNIQUE (source_disciple_id, target_disciple_id, relationship_type),
                                        CONSTRAINT chk_disciple_relationships_type CHECK (relationship_type IN ('PARENT_CHILD', 'INVITED_BY')),
                                        CONSTRAINT chk_disciple_relationships_not_self CHECK (source_disciple_id <> target_disciple_id)
);

CREATE INDEX idx_disciple_relationships_source ON disciple_relationships (source_disciple_id);
CREATE INDEX idx_disciple_relationships_target ON disciple_relationships (target_disciple_id);

-- Un discípulo solo puede tener UN invitador (a diferencia de PARENT_CHILD,
-- donde podría tener dos padres registrados). Índice único parcial, solo
-- aplica cuando relationship_type = 'INVITED_BY'.
CREATE UNIQUE INDEX uq_disciple_relationships_single_inviter
    ON disciple_relationships (target_disciple_id)
    WHERE relationship_type = 'INVITED_BY';

COMMENT ON TABLE disciple_relationships IS 'Relaciones entre discípulos: parentesco (PARENT_CHILD) e invitación (INVITED_BY)';
COMMENT ON COLUMN disciple_relationships.id IS 'Identificador único autogenerado de la relación';
COMMENT ON COLUMN disciple_relationships.source_disciple_id IS 'Discípulo origen: padre/madre en PARENT_CHILD, o quien invitó en INVITED_BY';
COMMENT ON COLUMN disciple_relationships.target_disciple_id IS 'Discípulo destino: hijo/a en PARENT_CHILD, o el discípulo invitado en INVITED_BY';
COMMENT ON COLUMN disciple_relationships.relationship_type IS 'Tipo de relación: PARENT_CHILD o INVITED_BY';
COMMENT ON COLUMN disciple_relationships.created_at IS 'Fecha y hora de creación del registro';
