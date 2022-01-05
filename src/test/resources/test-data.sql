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

