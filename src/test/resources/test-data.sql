TRUNCATE TABLE users;

INSERT INTO users (`id`, `first_name`, `last_name`, `email`, `password`, `position`, `department`,
                   `role`, `is_active`)
VALUES (1, 'John', 'Tudor', 'mail1@joy.com', 'pass', 'junior', 'HR', 'user', true),
       (2, 'Zoey', 'Aco', 'mail2@joy.com', 'pass', 'lead', 'IT', 'user', true),
       (3, 'John', 'Archibald', 'mail3@joy.com', 'pass', 'senior', 'IT', 'admin', true),
       (4, 'Artur', 'Conan', 'mail4@joy.com', 'pass', 'junior', 'IT', 'user', false),
       (5, 'john', 'little', 'mail5@joy.com', 'pass', 'junior', 'IT', 'user', true);