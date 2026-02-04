-- Создаём базы
CREATE DATABASE users;
CREATE DATABASE reminder;

-- users DB
\connect users;

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- reminder DB
\connect reminder;

CREATE TABLE IF NOT EXISTS reminders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    text VARCHAR(500) NOT NULL,
    remind_at TIMESTAMP NOT NULL
);