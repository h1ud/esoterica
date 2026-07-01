CREATE TABLE password_reset (
                                id BIGSERIAL PRIMARY KEY,
                                name VARCHAR(100) NOT NULL,
                                last_name VARCHAR(100) NOT NULL,
                                username VARCHAR(100) NOT NULL,
                                email VARCHAR(150) NOT NULL,
                                created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);