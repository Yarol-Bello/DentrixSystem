# 🦷 Dentrix System - Gestión Odontológica

Sistema de escritorio desarrollado en **JavaFX** para la gestión integral de un centro odontológico. Cumple con el patrón de arquitectura MVC, DAO, principios SOLID y pruebas unitarias TDD.

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **JavaFX 17** (Interfaz Gráfica)
- **PostgreSQL** (Base de Datos)
- **Maven** (Gestor de dependencias)
- **JUnit 5 & Mockito** (Pruebas Unitarias)
- **BCrypt** (Seguridad de contraseñas)
- **Apache POI** (Generación de reportes en Excel)
- **Logback** (Manejo de logs)
- **Google Guava** (Validaciones)

## 🏗️ Arquitectura

El proyecto sigue una arquitectura en capas:

- `com.dentrix.model`: Entidades POJO mapeadas a la BD.
- `com.dentrix.dao`: Interfaces e implementaciones con el SQL (Patrón DAO).
- `com.dentrix.service`: Lógica de negocio y validaciones.
- `com.dentrix.controller`: Controladores de eventos de la UI.
- `com.dentrix.view`: Vistas construidas puramente en JavaFX.
- `com.dentrix.util`: Conexión BD, encriptación y utilidades.

## 🔐 Seguridad

- **RBAC (Control de Acceso Basado en Roles):** El sistema adapta el menú lateral dependiendo de si el usuario es `ADMIN`, `ODONTOLOGO` o `RECEPCION`.
- Las contraseñas se almacenan en la BD utilizando **hash BCrypt**, nunca en texto plano.

## 🛠️ Instalación y Despliegue

1.  **Requisitos Previos:**
    - JDK 17 instalado.
    - PostgreSQL 14 o superior.
    - Maven instalado.

2.  **Base de Datos:**
    - Crea una base de datos en PostgreSQL llamada `dentrix_db`.
    - Ejecuta el script SQL que se encuentra en `src/main/resources/db/dentrix_db_full.sql`.

3.  **Configuración:**
    - Abre el archivo `src/main/resources/application.properties` y ajusta tus credenciales de PostgreSQL (`db.user` y `db.password`).

4.  **Ejecución:**
    ```bash
    mvn clean javafx:run
    ```
