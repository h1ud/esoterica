CREATE SEQUENCE IF NOT EXISTS promotion_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE promotion (
    id BIGINT PRIMARY KEY DEFAULT nextval('promotion_seq'),
    name VARCHAR(255) NOT NULL UNIQUE,
    day_of_week VARCHAR(20) NOT NULL,
    zodiac_signs VARCHAR(255) NOT NULL,
    description VARCHAR(500) NOT NULL,
    promo_price DOUBLE PRECISION NOT NULL,
    required_crepes INTEGER NOT NULL,
    required_fruits INTEGER NOT NULL,
    required_toppings INTEGER NOT NULL,
    topping_options VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL
);

INSERT INTO promotion (
    id,
    name,
    day_of_week,
    zodiac_signs,
    description,
    promo_price,
    required_crepes,
    required_fruits,
    required_toppings,
    topping_options,
    active
) VALUES
    (
        1,
        'Promo signos de tierra',
        'TUESDAY',
        'TAURO,VIRGO,CAPRICORNIO',
        '2 crepas + 2 frutas + 1 topping a eleccion',
        30.00,
        2,
        2,
        1,
        'MANJAR,MIEL',
        true
    ),
    (
        2,
        'Promo signos de aire',
        'WEDNESDAY',
        'ACUARIO,GEMINIS,LIBRA',
        '2 crepas + 2 frutas + 1 topping a eleccion',
        30.00,
        2,
        2,
        1,
        'MANJAR,MIEL',
        true
    ),
    (
        3,
        'Promo signos de agua',
        'THURSDAY',
        'CANCER,ESCORPIO,PISCIS',
        '2 crepas + 2 frutas + 1 topping a eleccion',
        30.00,
        2,
        2,
        1,
        'MANJAR,MIEL',
        true
    )
ON CONFLICT (name) DO NOTHING;

SELECT setval('promotion_seq', (SELECT MAX(id) FROM promotion));
