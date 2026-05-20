# URLs de admin

Base: `http://localhost:8080`

## Credenciales de arranque

Si todavia no existe ningun usuario con rol `ADMIN` o `ROLE_ADMIN`, se puede iniciar con:

- Usuario: `admin`
- Password: `admin123`

Variables opcionales:

- `ESOTERICA_ADMIN_USERNAME`
- `ESOTERICA_ADMIN_PASSWORD`
- `ESOTERICA_ADMIN_JWT_SECRET`

Cuando ya exista un usuario con rol `ADMIN` o `ROLE_ADMIN`, el login valida contra ese usuario y su `password_hash`.

## Login

**POST** `http://localhost:8080/api/admin/auth/login`

```json
{
  "username": "admin",
  "password": "admin123"
}
```

La respuesta devuelve un JWT. Usar el token en los endpoints protegidos:

```http
Authorization: Bearer <token>
```

## Usuarios

- **GET** `http://localhost:8080/api/admin/users`
- **GET** `http://localhost:8080/api/admin/users/1`
- **POST** `http://localhost:8080/api/admin/users`
- **PUT** `http://localhost:8080/api/admin/users/1`
- **DELETE** `http://localhost:8080/api/admin/users/1`

## Roles

- **GET** `http://localhost:8080/api/admin/roles`
- **GET** `http://localhost:8080/api/admin/roles/1`
- **POST** `http://localhost:8080/api/admin/roles`
- **PUT** `http://localhost:8080/api/admin/roles/1`
- **DELETE** `http://localhost:8080/api/admin/roles/1`
