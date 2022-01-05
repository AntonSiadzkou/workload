//TRUNCATE TABLE departments;
//TRUNCATE TABLE users;

INSERT INTO departments(id, title) VALUES (1, 'HR'), (2, 'PR'), (3, 'IT');


INSERT INTO users (`id`, `first_name`, `last_name`, `email`, `password`, `position`, `department`,
                   `role`, `is_active`)
VALUES (1, 'John', 'Tudor', 'mail1@joy.com', 'pass24WQ', 'junior', 1, 'user', true),
       (2, 'Zoey', 'Aco', 'mail2@joy.com', 'pass24WQ', 'lead', 3, 'user', true),
       (3, 'John', 'Archibald', 'mail3@joy.com', 'pass24WQ', 'senior', 3, 'admin', true),
       (4, 'Artur', 'Conan', 'mail4@joy.com', 'pass24WQ', 'junior', 3, 'user', false),
       (5, 'john', 'little', 'mail5@joy.com', 'pass24WQ', 'junior', 3, 'user', true);

INSERT INTO projects (id, name, start_date, end_date) VALUES (1, 'java project', '2021-10-28', '2022-02-28');
INSERT INTO projects (id, name, start_date, end_date) VALUES (2, 'js project', '2021-11-09', '2023-05-28');
