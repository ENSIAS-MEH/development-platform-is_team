-- ============================================================
-- V3 : Fonctionnalités M2 (roadmaps, sessions, messages, ratings)
-- ============================================================

CREATE TABLE roadmaps (
    id          BIGSERIAL    PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    filiere     VARCHAR(100),
    mentor_id   BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roadmap_steps (
    id          BIGSERIAL    PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    step_order  INTEGER      NOT NULL,
    roadmap_id  BIGINT       NOT NULL REFERENCES roadmaps(id) ON DELETE CASCADE
);

CREATE TABLE roadmap_enrollments (
    id               BIGSERIAL PRIMARY KEY,
    student_id       BIGINT    NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    roadmap_id       BIGINT    NOT NULL REFERENCES roadmaps(id) ON DELETE CASCADE,
    progress_percent INTEGER   NOT NULL DEFAULT 0,
    enrolled_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (student_id, roadmap_id)
);

CREATE TABLE sessions (
    id           BIGSERIAL   PRIMARY KEY,
    student_id   BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    mentor_id    BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    session_date TIMESTAMP   NOT NULL,
    message      TEXT,
    status       VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'ACCEPTED', 'DECLINED')),
    created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE conversations (
    id         BIGSERIAL PRIMARY KEY,
    student_id BIGINT    NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    mentor_id  BIGINT    NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (student_id, mentor_id)
);

CREATE TABLE messages (
    id              BIGSERIAL PRIMARY KEY,
    conversation_id BIGINT    NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    sender_id       BIGINT    NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content         TEXT      NOT NULL,
    sent_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ratings (
    id         BIGSERIAL PRIMARY KEY,
    student_id BIGINT    NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    mentor_id  BIGINT    NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    score      INTEGER   NOT NULL CHECK (score BETWEEN 1 AND 5),
    comment    TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (student_id, mentor_id)
);

CREATE INDEX idx_roadmaps_mentor ON roadmaps(mentor_id);
CREATE INDEX idx_sessions_student ON sessions(student_id);
CREATE INDEX idx_sessions_mentor ON sessions(mentor_id);
CREATE INDEX idx_messages_conversation ON messages(conversation_id);
