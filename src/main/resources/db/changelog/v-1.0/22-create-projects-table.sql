CREATE TABLE projects
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR   NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date   TIMESTAMP
);
END