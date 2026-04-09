# AdoptaPerrito API

Backend de la plataforma de adopción de perros y gatos, desarrollado con Kotlin y Spring Boot.

## Tecnología
- Kotlin
- Spring Boot 4
- PostgreSQL 14
- JPA / Hibernate
- Maven

## Requisitos previos
- JDK 21
- PostgreSQL instalado y corriendo
- Archivo `.env` en la raíz del proyecto con las siguientes variables:
```
  URL_DB=localhost:5432/adopta_perrito_db
  USER_DB=tu_usuario_postgres
  PASSWORD_DB=tu_password_postgres
```

## Base de datos

Para construir la base de datos desde cero, copiar el script a un directorio accesible y ejecutarlo, ejemplo:
```bash
cp database/schema.sql /tmp/schema.sql
sudo -u postgres psql -f /tmp/schema.sql
```

Esto creará la base de datos `adopta_perrito_db` con la tabla `usuario` y datos de prueba.

## Cómo ejecutar

1. Clonar el repositorio.
2. Crear el archivo `.env` en la raíz con las credenciales de tu PostgreSQL local.
3. Ejecutar la base de datos con el script `database/schema.sql`.
4. Levantar el proyecto desde IntelliJ o con:
```bash
   ./mvnw spring-boot:run
```
5. La API estará disponible en `http://localhost:8080`.

## Endpoints disponibles

| Método | URL                    | Descripción                            |
|--------|------------------------|----------------------------------------|
| POST   | /usuarios/register     | Registra un nuevo usuario              |
| POST   | /usuarios/login        | Inicia sesión y retorna un token       |
| POST   | /usuarios/logout       | Cierra sesión e invalida el token      |
| GET    | /usuarios/me           | Retorna el usuario autenticado         |
| GET    | /usuarios/all          | Retorna todos los usuarios             |
| GET    | /usuarios/{id}         | Busca un usuario por ID                |
| GET    | /usuarios/buscar       | Filtra usuarios por email, cp o nombre |
