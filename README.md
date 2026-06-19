# Esoterica Backend

Backend REST para gestionar productos, usuarios admin y promociones zodiacales de crepas.

El proyecto usa Spring Boot con PostgreSQL. Las tablas y datos iniciales se crean con Flyway, asi que en una PC nueva basta con crear la base de datos, ajustar `application.properties` y levantar la app.

## Como levantar

Crear la base de datos:

```sql
CREATE DATABASE dbesoterica;
```

Ajustar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/dbesoterica
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD
```

Levantar backend:

```powershell
.\gradlew.bat bootRun
```

Ejecutar pruebas:

```powershell
.\gradlew.bat test
```

Base local:

```text
http://localhost:8080
```

## Stack

| Tecnologia | Uso |
| --- | --- |
| Java 21 | Runtime |
| Spring Boot 3.2.5 | Framework backend |
| Spring Web | Controllers REST |
| Spring Data JPA | Repositories y persistencia |
| PostgreSQL | Base de datos |
| Flyway | Migraciones y semillas |
| Lombok | DTOs y entidades |
| MapStruct | Mapeo entidad/DTO |
| JUnit 5 + MockMvc | Pruebas |

## Migraciones y semillas

Flyway ejecuta:

| Archivo | Contenido |
| --- | --- |
| `V1__tableclients.sql` | Roles, usuarios, productos, clientes y tablas futuras de ventas |
| `V2__fixed_zodiac_promotions.sql` | Promociones zodiacales |

Usuarios semilla:

| Usuario | Password | Rol |
| --- | --- | --- |
| `admin` | `admin123` | `ADMIN` |
| `alice` | `password1` | `USER` |
| `bob` | `password2` | `USER` |

Promociones semilla:

| Dia | Signos | Oferta |
| --- | --- | --- |
| Martes | Tauro, Virgo, Capricornio | 2 crepas + 2 frutas + 1 topping por 30 |
| Miercoles | Acuario, Geminis, Libra | 2 crepas + 2 frutas + 1 topping por 30 |
| Jueves | Cancer, Escorpio, Piscis | 2 crepas + 2 frutas + 1 topping por 30 |

## Login y JWT

Un solo login autentica usuarios y admins:

```http
POST /api/auth/login
```

Body:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Respuesta:

```json
{
  "token": "...",
  "tokenType": "Bearer",
  "expiresInSeconds": 7200,
  "username": "admin",
  "role": "ADMIN"
}
```

Para endpoints admin:

```http
Authorization: Bearer <token>
```

Reglas:

| Caso | Respuesta |
| --- | --- |
| Sin JWT o JWT invalido | `401 Unauthorized` |
| JWT valido con rol `USER` en rutas admin | `403 Forbidden` |
| JWT valido con rol `ADMIN` | Acceso permitido |

El JWT se firma en `JwtService` con HS256. Usa los claims `sub`, `role`, `iss`, `iat` y `exp`.

Variable recomendada:

```text
ESOTERICA_JWT_SECRET
```

## Poderes por rol

### Usuario normal

| Accion | Metodo | Endpoint |
| --- | --- | --- |
| Login | `POST` | `/api/auth/login` |
| Listar productos | `GET` | `/api/products` |
| Ver producto por ID | `GET` | `/api/products/{id}` |
| Buscar productos | `GET` | `/api/products/search?name=...` |
| Listar promociones activas | `GET` | `/api/promotions` |
| Promos del dia | `GET` | `/api/promotions/today` |
| Promos por fecha | `GET` | `/api/promotions/today?date=yyyy-MM-dd` |
| Validar promo por signo | `GET` | `/api/promotions/validate?sign=TAURO&date=yyyy-MM-dd` |
| Cotizar promo | `POST` | `/api/promotions/quote` |

### Admin

Tiene todo lo publico y ademas:

| Accion | Metodo | Endpoint |
| --- | --- | --- |
| Listar usuarios | `GET` | `/api/admin/users` |
| Ver usuario | `GET` | `/api/admin/users/{id}` |
| Crear usuario | `POST` | `/api/admin/users` |
| Actualizar usuario | `PUT` | `/api/admin/users/{id}` |
| Eliminar usuario | `DELETE` | `/api/admin/users/{id}` |
| Listar promociones admin | `GET` | `/api/admin/promotions` |
| Ver promocion | `GET` | `/api/admin/promotions/{id}` |
| Crear promocion | `POST` | `/api/admin/promotions` |
| Actualizar promocion | `PUT` | `/api/admin/promotions/{id}` |
| Eliminar promocion | `DELETE` | `/api/admin/promotions/{id}` |

## Contrato frontend

### LoginRequestDTO

```json
{
  "username": "alice",
  "password": "password1"
}
```

### LoginResponseDTO

```json
{
  "token": "...",
  "tokenType": "Bearer",
  "expiresInSeconds": 7200,
  "username": "alice",
  "role": "USER"
}
```

### productDTO

```json
{
  "id": 1,
  "product_name": "Vela ritual",
  "price": 15.0,
  "activation": "2026-05-25T09:00:00",
  "expiration": "2027-05-25T09:00:00"
}
```

### userDTO

```json
{
  "id": 2,
  "username": "alice",
  "password_hash": "password1",
  "first_name": "Alice",
  "last_name": "Garcia",
  "role": {
    "id": 2,
    "role_name": "USER"
  },
  "create_date": "2026-05-26T20:00:00"
}
```

### promotionDTO

```json
{
  "name": "Promo signos de tierra",
  "day_of_week": "MARTES",
  "zodiac_signs": ["TAURO", "VIRGO", "CAPRICORNIO"],
  "description": "2 crepas + 2 frutas + 1 topping a eleccion",
  "promo_price": 30.0,
  "required_crepes": 2,
  "required_fruits": 2,
  "required_toppings": 1,
  "topping_options": ["MANJAR", "MIEL"],
  "active": true
}
```

Notas:

- `day_of_week` acepta espanol o ingles.
- `zodiac_signs` y `topping_options` se normalizan a mayusculas y sin tildes.
- `PICIS` se corrige internamente a `PISCIS`.
- `active=false` oculta una promocion del flujo publico sin borrarla.
- `GET /api/promotions` devuelve solo promociones activas.
- `GET /api/admin/promotions` devuelve activas e inactivas.

### promotionQuoteRequest

```json
{
  "zodiac_sign": "TAURO",
  "topping": "MANJAR",
  "date": "2026-05-26"
}
```

## Endpoints retirados

Ya no forman parte del flujo:

```text
/api/username
/api/roles
/api/admin/roles
/api/admin/auth/login
```

La gestion de usuarios esta centralizada en `/api/admin/users` y el login esta centralizado en `/api/auth/login`.
