# URLs de prueba REST

Base: `http://localhost:8080`

## Login unico
- **POST** `http://localhost:8080/api/auth/login`

## Productos publicos
- **GET** `http://localhost:8080/api/products`
- **GET** `http://localhost:8080/api/products/1`
- **GET** `http://localhost:8080/api/products/search?name=Test`
- **POST** `http://localhost:8080/api/products`
- **PUT** `http://localhost:8080/api/products/1`
- **DELETE** `http://localhost:8080/api/products/1`

## Promociones publicas
- **GET** `http://localhost:8080/api/promotions`
- **GET** `http://localhost:8080/api/promotions/today`
- **GET** `http://localhost:8080/api/promotions/today?date=2026-05-26`
- **GET** `http://localhost:8080/api/promotions/validate?sign=TAURO&date=2026-05-26`
- **POST** `http://localhost:8080/api/promotions/quote`

## Admin usuarios
- **GET** `http://localhost:8080/api/admin/users`
- **GET** `http://localhost:8080/api/admin/users/1`
- **POST** `http://localhost:8080/api/admin/users`
- **PUT** `http://localhost:8080/api/admin/users/1`
- **DELETE** `http://localhost:8080/api/admin/users/1`

## Admin promociones
- **GET** `http://localhost:8080/api/admin/promotions`
- **GET** `http://localhost:8080/api/admin/promotions/1`
- **POST** `http://localhost:8080/api/admin/promotions`
- **PUT** `http://localhost:8080/api/admin/promotions/1`
- **DELETE** `http://localhost:8080/api/admin/promotions/1`
