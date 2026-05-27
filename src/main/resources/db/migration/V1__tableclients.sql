CREATE SEQUENCE IF NOT EXISTS role_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS username_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS product_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS client_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS discounts_code_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS close_session_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS sale_operation_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS sale_detail_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE role (
    id BIGINT PRIMARY KEY DEFAULT nextval('role_seq'),
    role_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE username (
    id BIGINT PRIMARY KEY DEFAULT nextval('username_seq'),
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    create_date TIMESTAMP,
    role_id BIGINT NOT NULL REFERENCES role(id)
);

CREATE TABLE product (
    id BIGINT PRIMARY KEY DEFAULT nextval('product_seq'),
    product_name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    activation TIMESTAMP,
    expiration TIMESTAMP
);

CREATE TABLE client (
    id BIGINT PRIMARY KEY DEFAULT nextval('client_seq'),
    client_name VARCHAR(255),
    password_hash VARCHAR(255),
    dni VARCHAR(255),
    birthday_date DATE
);

CREATE TABLE discounts_code (
    id BIGINT PRIMARY KEY DEFAULT nextval('discounts_code_seq'),
    code_name VARCHAR(255),
    code_value INTEGER NOT NULL,
    is_activate BOOLEAN,
    is_use BOOLEAN,
    created_at TIMESTAMP,
    client_id BIGINT REFERENCES client(id)
);

CREATE TABLE close_session (
    id BIGINT PRIMARY KEY DEFAULT nextval('close_session_seq'),
    emission_date TIMESTAMP,
    total DOUBLE PRECISION NOT NULL,
    user_id BIGINT REFERENCES username(id)
);

CREATE TABLE sale_operation (
    id BIGINT PRIMARY KEY DEFAULT nextval('sale_operation_seq'),
    issue_date TIMESTAMP,
    user_id BIGINT REFERENCES username(id)
);

CREATE TABLE sale_detail (
    id BIGINT PRIMARY KEY DEFAULT nextval('sale_detail_seq'),
    quantity INTEGER NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    product_id BIGINT REFERENCES product(id),
    sale_operation_id BIGINT REFERENCES sale_operation(id)
);

INSERT INTO role (id, role_name) VALUES
    (1, 'ADMIN'),
    (2, 'USER')
ON CONFLICT (role_name) DO NOTHING;

INSERT INTO username (id, username, password_hash, first_name, last_name, create_date, role_id) VALUES
    (1, 'admin', 'admin123', 'Admin', 'Principal', CURRENT_TIMESTAMP, 1),
    (2, 'alice', 'password1', 'Alice', 'Garcia', CURRENT_TIMESTAMP, 2),
    (3, 'bob', 'password2', 'Bob', 'Lopez', CURRENT_TIMESTAMP, 2)
ON CONFLICT (username) DO NOTHING;

INSERT INTO product (id, product_name, price, activation, expiration) VALUES
    (1, 'Galletas', 2.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 year'),
    (2, 'Jugo', 1.75, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 year'),
    (3, 'Vela ritual', 15.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 year');

SELECT setval('role_seq', (SELECT MAX(id) FROM role));
SELECT setval('username_seq', (SELECT MAX(id) FROM username));
SELECT setval('product_seq', (SELECT MAX(id) FROM product));
