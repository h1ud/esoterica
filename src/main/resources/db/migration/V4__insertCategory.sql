INSERT INTO "category" (id, category_name, description) VALUES
                                                            (1, 'Sándwiches', NULL),
                                                            (2, 'Empanadas', NULL),
                                                            (3, 'Choripanes', NULL),
                                                            (4, 'Porteñitos', NULL),
                                                            (5, 'Cremas', NULL),
                                                            (6, 'Toppings', NULL),
                                                            (7, 'Postres Helados', NULL),
                                                            (8, 'Bebidas', NULL),
                                                            (9, 'Especiales', NULL)
    ON CONFLICT (id) DO NOTHING;