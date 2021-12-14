CREATE TABLE users
(
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR NOT NULL,
    last_name     VARCHAR NOT NULL,
    email         VARCHAR NOT NULL UNIQUE,
    password      VARCHAR NOT NULL,
    position      VARCHAR NOT NULL,
    id_department BIGINT  NOT NULL,
    role          VARCHAR NOT NULL,
    is_active     BOOLEAN DEFAULT false,
    FOREIGN KEY (id_department) REFERENCES departments (id)
);
END