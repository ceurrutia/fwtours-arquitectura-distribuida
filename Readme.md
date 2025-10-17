# FW Tours - Arquitectura Distribuida

Este proyecto es un ejemplo de arquitectura distribuida con microservicios desarrollado en **Java Spring Boot**. Incluye los siguientes componentes:

- **Config Server**: Centraliza la configuración de los microservicios.
- **Discovery Server (Eureka)**: Registro y descubrimiento de servicios.
- **API Gateway**: Punto de entrada único para los clientes.
- **Microservicios**: Actualmente incluye `user-service` para gestión de usuarios.
- **Base de datos**: MySQL para persistencia de datos.

---

## Tecnologías usadas

- Java 17
- Spring Boot 3.x
- Spring Cloud Config
- Eureka Discovery
- Spring Data JPA / Hibernate
- MySQL
- Maven
- Postman (para pruebas)

---

## Endpoints de user-service

* POST	/users/register	Crear un nuevo usuario
* GET	/users	Listar todos los usuarios
* GET	/users/{id}	Obtener usuario por ID
* PUT	/users/{id}	Actualizar un usuario
* DELETE	/users/{id}	Eliminar un usuario

---

## Cómo ejecutar

* Levantar MySQL y crear la base de datos db_users.
* Ejecutar Config Server (8888).
* Ejecutar Discovery Server / Eureka (8761).
* Ejecutar API Gateway (8080).
* Ejecutar user-service (8081).
* Probar los endpoints usando Postman.

---
## Notas

El user-service utiliza roles (ADMIN, CLIENTE, AGENT) y status (ACTIVO, INACTIVO).