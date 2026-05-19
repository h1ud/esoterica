CREATE TABLE username (
    id BIGSERIAL PRIMARY KEY,

    username VARCHAR(255),
    password_hash VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),

    create_date TIMESTAMP,

);