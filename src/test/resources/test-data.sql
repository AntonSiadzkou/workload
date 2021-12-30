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
INSERT INTO projects (id, name, start_date, end_date) VALUES (2, 'kotlin project', '2022-01-01', '2023-01-01');

INSERT INTO public.user_project (id_user, id_project, assign_date, cancel_date) VALUES (1, 2, '2021-12-31', '2022-02-01');
INSERT INTO public.user_project (id_user, id_project, assign_date, cancel_date) VALUES (2, 2, '2021-12-23', '2022-03-27');
INSERT INTO public.user_project (id_user, id_project, assign_date, cancel_date) VALUES (3, 2, '2021-01-31', '2022-05-01');
INSERT INTO public.user_project (id_user, id_project, assign_date, cancel_date) VALUES (4, 2, '2022-02-20', '2022-06-30');
INSERT INTO public.user_project (id_user, id_project, assign_date, cancel_date) VALUES (5, 1, '2021-12-31', '2022-08-01');
INSERT INTO public.user_project (id_user, id_project, assign_date, cancel_date) VALUES (6, 1, '2022-03-01', '2022-04-01');
INSERT INTO public.user_project (id_user, id_project, assign_date, cancel_date) VALUES (1, 1, '2021-02-02', '2022-05-01');
INSERT INTO public.user_project (id_user, id_project, assign_date, cancel_date) VALUES (4, 1, '2020-01-01', '2020-02-06');
