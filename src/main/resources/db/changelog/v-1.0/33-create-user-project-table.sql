CREATE TABLE user_project
(
    id_user     BIGINT,
    id_project  BIGINT,
    assign_date TIMESTAMP NOT NULL,
    cancel_date TIMESTAMP NOT NULL,
    PRIMARY KEY (id_user, id_project),
    FOREIGN KEY (id_user) REFERENCES users (id),
    FOREIGN KEY (id_project) REFERENCES projects (id)
);
END