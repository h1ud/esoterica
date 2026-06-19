INSERT INTO "role" ("id", "role_name") VALUES (1, 'ADMIN'), (2, 'EMPLOYEE'), (3, 'MANAGER');

INSERT INTO "user" ("id", "id_role", "username", "password_hash", "name", "last_name", "create_date")
VALUES
    (1, 1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gBS7O2', 'Admin', 'User', NOW()),
    (2, 2, 'employee1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gBS7O2', 'Juan', 'López', NOW()),
    (3, 2, 'employee2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gBS7O2', 'María', 'García', NOW());

INSERT INTO "client" ("id", "name", "password_hash", "dni", "birthday_date", "create_date")
VALUES
    (1, 'Carlos Mendez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gBS7O2', '12345678', '1990-05-15', NOW()),
    (2, 'Ana Morales', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gBS7O2', '87654321', '1988-08-22', NOW()),
    (3, 'Pedro Sánchez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36gBS7O2', '11223344', '1995-12-10', NOW());

