-- ============================================================================
-- users
-- ============================================================================
CREATE TABLE users (
                       id                       BIGINT GENERATED ALWAYS AS IDENTITY,
                       username                 VARCHAR(50)  NOT NULL,
                       email                    VARCHAR(150) NOT NULL,
                       password_hash            VARCHAR(255) NOT NULL,
                       first_name               VARCHAR(150) NOT NULL,
                       last_name                VARCHAR(150) NOT NULL,
                       gender                   VARCHAR(10)  NOT NULL,
                       role                     VARCHAR(20)  NOT NULL DEFAULT 'PASTOR',
                       enabled                  BOOLEAN NOT NULL DEFAULT TRUE,
                       account_non_expired      BOOLEAN NOT NULL DEFAULT TRUE,
                       account_non_locked       BOOLEAN NOT NULL DEFAULT TRUE,
                       credentials_non_expired  BOOLEAN NOT NULL DEFAULT TRUE,
                       last_login_at            TIMESTAMP,
                       created_at               TIMESTAMP NOT NULL DEFAULT now(),
                       updated_at               TIMESTAMP NOT NULL DEFAULT now(),

                       CONSTRAINT pk_users PRIMARY KEY (id),
                       CONSTRAINT uq_users_username UNIQUE (username),
                       CONSTRAINT uq_users_email UNIQUE (email),
                       CONSTRAINT chk_users_role CHECK (role IN ('PASTOR')),
                       CONSTRAINT chk_users_gender CHECK (gender IN ('MALE', 'FEMALE'))
);

-- ============================================================================
-- users - comentarios
-- ============================================================================
COMMENT ON TABLE users IS 'Cuentas con acceso al sistema. Uso exclusivo del pastor y la pastora';
COMMENT ON COLUMN users.id IS 'Identificador único autogenerado de la cuenta de usuario';
COMMENT ON COLUMN users.username IS 'Nombre de usuario único para iniciar sesión, alternativo al email';
COMMENT ON COLUMN users.email IS 'Correo electrónico único, usado para iniciar sesión y recuperación de contraseña';
COMMENT ON COLUMN users.password_hash IS 'Hash BCrypt de la contraseña';
COMMENT ON COLUMN users.first_name IS 'Nombre del pastor o pastora';
COMMENT ON COLUMN users.last_name IS 'Apellido del pastor o pastora';
COMMENT ON COLUMN users.gender IS 'Género del pastor o pastora: MALE o FEMALE';
COMMENT ON COLUMN users.role IS 'Rol del usuario. Único valor posible actualmente: PASTOR';
COMMENT ON COLUMN users.enabled IS 'Indica si la cuenta está habilitada para iniciar sesión (Spring Security UserDetails.isEnabled)';
COMMENT ON COLUMN users.account_non_expired IS 'Indica si la cuenta no ha expirado (Spring Security UserDetails.isAccountNonExpired)';
COMMENT ON COLUMN users.account_non_locked IS 'Indica si la cuenta no está bloqueada (Spring Security UserDetails.isAccountNonLocked)';
COMMENT ON COLUMN users.credentials_non_expired IS 'Indica si las credenciales no han expirado (Spring Security UserDetails.isCredentialsNonExpired)';
COMMENT ON COLUMN users.last_login_at IS 'Fecha y hora del último inicio de sesión exitoso, usado como referencia informativa';
COMMENT ON COLUMN users.created_at IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN users.updated_at IS 'Fecha y hora de la última modificación del registro';