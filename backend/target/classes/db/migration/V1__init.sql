-- ============================================================
-- V1 : Schéma initial MentorPath
-- ============================================================

CREATE TABLE users (
    id         BIGSERIAL    PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL CHECK (role IN ('STUDENT','MENTOR','ADMIN')),
    enabled    BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE student_profiles (
    id          BIGSERIAL    PRIMARY KEY,
    user_id     BIGINT       NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    first_name  VARCHAR(100),
    last_name   VARCHAR(100),
    filiere     VARCHAR(100),
    annee_etude INTEGER,
    bio         TEXT,
    avatar_url  VARCHAR(500)
);

CREATE TABLE mentor_profiles (
    id          BIGSERIAL      PRIMARY KEY,
    user_id     BIGINT         NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    first_name  VARCHAR(100),
    last_name   VARCHAR(100),
    filiere     VARCHAR(100),
    promo       INTEGER,
    bio         TEXT,
    expertise   TEXT,
    rating      DECIMAL(3,2)   NOT NULL DEFAULT 0.0,
    available   BOOLEAN        NOT NULL DEFAULT TRUE,
    avatar_url  VARCHAR(500)
);
