# URLs de admin

Base: `http://localhost:8080`

## Login unico

El proyecto usa un solo login para usuarios y administradores:

**POST** `http://localhost:8080/api/auth/login`

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Usar el JWT devuelto:

```http
Authorization: Bearer <token>
```

## Usuarios

- **GET** `http://localhost:8080/api/admin/users`
- **GET** `http://localhost:8080/api/admin/users/1`
- **POST** `http://localhost:8080/api/admin/users`
- **PUT** `http://localhost:8080/api/admin/users/1`
- **DELETE** `http://localhost:8080/api/admin/users/1`

## Promociones

- **GET** `http://localhost:8080/api/admin/promotions`
- **GET** `http://localhost:8080/api/admin/promotions/1`
- **POST** `http://localhost:8080/api/admin/promotions`
- **PUT** `http://localhost:8080/api/admin/promotions/1`
- **DELETE** `http://localhost:8080/api/admin/promotions/1`
