-- ============================================================================
-- retreats
-- ============================================================================
CREATE TABLE retreats (
                          id              BIGINT GENERATED ALWAYS AS IDENTITY,

                          name            VARCHAR(150) NOT NULL,
                          location        VARCHAR(255) NOT NULL,
                          price           NUMERIC(10,2) NOT NULL DEFAULT 0,
                          start_date      TIMESTAMPTZ NOT NULL,
                          end_date        TIMESTAMPTZ NOT NULL,

                          created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
                          updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),

                          CONSTRAINT pk_retreats PRIMARY KEY (id),
                          CONSTRAINT chk_retreats_dates CHECK (end_date > start_date)
);

CREATE INDEX idx_retreats_start_date ON retreats (start_date);

COMMENT ON TABLE retreats IS 'Encuentro: evento con fechas y lugar definido, con discípulos inscritos como asistentes o como staff';
COMMENT ON COLUMN retreats.id IS 'Identificador único autogenerado del encuentro';
COMMENT ON COLUMN retreats.name IS 'Nombre del encuentro';
COMMENT ON COLUMN retreats.location IS 'Lugar donde se realiza el encuentro';
COMMENT ON COLUMN retreats.price IS 'Costo de inscripción al encuentro, aplicado a todos los asistentes';
COMMENT ON COLUMN retreats.start_date IS 'Fecha y hora de inicio del encuentro (viernes por la noche)';
COMMENT ON COLUMN retreats.end_date IS 'Fecha y hora de finalización del encuentro (domingo aproximadamente a las 6:00 p.m.)';
COMMENT ON COLUMN retreats.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN retreats.updated_at IS 'Fecha y hora de la última modificación del registro';

-- ============================================================================
-- retreat_enrollments
-- ============================================================================
CREATE TABLE retreat_enrollments (
                                     id                  BIGINT GENERATED ALWAYS AS IDENTITY,
                                     retreat_id          BIGINT NOT NULL,
                                     disciple_id         BIGINT NOT NULL,

                                     payment_status      VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                                     amount_paid         NUMERIC(10,2) NOT NULL DEFAULT 0,

                                     created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
                                     updated_at          TIMESTAMPTZ NOT NULL DEFAULT now(),

                                     CONSTRAINT pk_retreat_enrollments PRIMARY KEY (id),
                                     CONSTRAINT fk_retreat_enrollments_retreat FOREIGN KEY (retreat_id)
                                         REFERENCES retreats(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_retreat_enrollments_disciple FOREIGN KEY (disciple_id)
                                         REFERENCES disciples(id) ON DELETE CASCADE,
                                     CONSTRAINT uq_retreat_enrollments_retreat_disciple UNIQUE (retreat_id, disciple_id),
                                     CONSTRAINT chk_retreat_enrollments_payment_status CHECK (payment_status IN ('PAID', 'PENDING', 'PARTIAL'))
);

CREATE INDEX idx_retreat_enrollments_retreat_id ON retreat_enrollments (retreat_id);
CREATE INDEX idx_retreat_enrollments_disciple_id ON retreat_enrollments (disciple_id);
CREATE INDEX idx_retreat_enrollments_payment_status ON retreat_enrollments (payment_status);

COMMENT ON TABLE retreat_enrollments IS 'Discípulos inscritos como asistentes a un encuentro, con estado de pago';
COMMENT ON COLUMN retreat_enrollments.id IS 'Identificador único autogenerado de la inscripción';
COMMENT ON COLUMN retreat_enrollments.retreat_id IS 'Encuentro al que asiste el discípulo';
COMMENT ON COLUMN retreat_enrollments.disciple_id IS 'Discípulo inscrito como asistente';
COMMENT ON COLUMN retreat_enrollments.payment_status IS 'Estado de pago del discípulo: PAID (pagado) o PENDING (pendiente de pago)';
COMMENT ON COLUMN retreat_enrollments.amount_paid IS 'Monto acumulado pagado por el discípulo hasta el momento';
COMMENT ON COLUMN retreat_enrollments.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN retreat_enrollments.updated_at IS 'Fecha y hora de la última modificación del registro';


-- ============================================================================
-- retreat_staff
-- ============================================================================
CREATE TABLE retreat_staff (
                               id                  BIGINT GENERATED ALWAYS AS IDENTITY,
                               retreat_id          BIGINT NOT NULL,
                               disciple_id         BIGINT NOT NULL,

                               created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
                               updated_at          TIMESTAMPTZ NOT NULL DEFAULT now(),

                               CONSTRAINT pk_retreat_staff PRIMARY KEY (id),
                               CONSTRAINT fk_retreat_staff_retreat FOREIGN KEY (retreat_id)
                                   REFERENCES retreats(id) ON DELETE CASCADE,
                               CONSTRAINT fk_retreat_staff_disciple FOREIGN KEY (disciple_id)
                                   REFERENCES disciples(id) ON DELETE CASCADE,
                               CONSTRAINT uq_retreat_staff_retreat_disciple UNIQUE (retreat_id, disciple_id)
);

CREATE INDEX idx_retreat_staff_retreat_id ON retreat_staff (retreat_id);
CREATE INDEX idx_retreat_staff_disciple_id ON retreat_staff (disciple_id);

COMMENT ON TABLE retreat_staff IS 'Discípulos que sirven de apoyo/staff durante un encuentro, registro independiente de los asistentes';
COMMENT ON COLUMN retreat_staff.id IS 'Identificador único autogenerado del registro de staff';
COMMENT ON COLUMN retreat_staff.retreat_id IS 'Encuentro en el que sirve el discípulo';
COMMENT ON COLUMN retreat_staff.disciple_id IS 'Discípulo que sirve de apoyo/staff';
COMMENT ON COLUMN retreat_staff.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN retreat_staff.updated_at IS 'Fecha y hora de la última modificación del registro';