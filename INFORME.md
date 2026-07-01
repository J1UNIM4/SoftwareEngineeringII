# Personal Finance Management — Proyecto Final Ingeniería de Software II (2026)

Aplicación web full-stack (Spring Boot + React) para la gestión de finanzas personales,
evolucionada con un **pipeline de CI/CD** y prácticas de calidad y seguridad de software.

> El tutorial conceptual original sobre Domain-Driven Design (DDD) se conserva en
> [`docs/DDD-Tutorial.md`](docs/DDD-Tutorial.md). Este documento es el **entregable del proyecto final**;
> el [`Readme.md`](Readme.md) del repositorio resume este mismo contenido para la portada de GitHub.

---

## Índice

1. [Equipo de trabajo](#1-equipo-de-trabajo)
2. [Propósito del proyecto](#2-propósito-del-proyecto)
3. [Funcionalidades](#3-funcionalidades)
4. [Modelo de dominio y arquitectura](#4-modelo-de-dominio-y-arquitectura)
5. [Módulos y servicios REST](#5-módulos-y-servicios-rest)
6. [Tecnologías](#6-tecnologías)
7. [Cómo levantar la aplicación](#7-cómo-levantar-la-aplicación)
8. [Calidad de código y análisis estático](#8-calidad-de-código-y-análisis-estático)
9. [Pruebas](#9-pruebas)
10. [Seguridad — vulnerabilidades y correcciones](#10-seguridad--vulnerabilidades-y-correcciones)
11. [Pipeline CI/CD](#11-pipeline-cicd)
12. [Gestión de tareas y releases](#12-gestión-de-tareas-y-releases)
13. [Migración a Arquitectura Modular DDD (Práctica 07)](#13-migración-a-arquitectura-modular-ddd-práctica-07)

---

## 1. Equipo de trabajo

| Integrante | Rol (Proyecto Final) | Rol (Práctica 07 — DDD) |
|------------|------------------------|--------------------------|
| Barreros Rodríguez, Olga Angélica | Persona 3 — QA: validación, funcionales, performance | Pruebas funcionales/performance/seguridad, migración `modules/person` |
| Morales Huanca, Jossein | Persona 4 — Calidad: SonarQube, dependencias | Refactoring y análisis estático, migración `modules/group` |
| Paredes Hallasi, Karoline Mishell | Persona 2 — Seguridad: auth, CORS, OWASP ZAP | Migración `modules/scheduling`, fix de seguridad (SecurityConfig) |
| Reinoso Bengoa, Joel Andrés | Persona 1 — DevOps: pipeline Jenkins, build, Docker | Migración `modules/ledger` (Account, Category, Ledger, Transaction) |

## 2. Propósito del proyecto

Plataforma para registrar y gestionar las **finanzas personales y de grupos** (familias):
personas, grupos, cuentas, categorías, transacciones (crédito/débito) y planificaciones,
implementada bajo una arquitectura **Domain-Driven Design (DDD)** por capas.

## 3. Funcionalidades

- Registro y consulta de personas y sus relaciones (familia/hermanos).
- Creación de grupos con administradores y libro contable (*ledger*).
- Gestión de cuentas y categorías por persona y por grupo.
- Registro, actualización y borrado de transacciones (crédito/débito).
- Consulta de movimientos por cuenta y rango de fechas.

> Diagramas UML (casos de uso, clases, secuencia) disponibles en la carpeta [`diagrams/`](diagrams).

## 4. Modelo de dominio y arquitectura

Arquitectura **DDD por capas**:

```
controllerLayer     → Controladores REST (entrada/salida HTTP)
applicationLayer    → Servicios de aplicación (orquestación de casos de uso)
domainLayer         → Entidades, Value Objects, Agregados (núcleo del negocio)
infrastructureLayer → Repositorios JPA y persistencia (H2)
dtos                → Data Transfer Objects + Assemblers entre capas
```

Conceptos DDD aplicados: **Entidades, Value Objects, Agregados (un repositorio por agregado),
Servicios y DTOs**. Detalle conceptual en [`docs/DDD-Tutorial.md`](docs/DDD-Tutorial.md) y [`UML.md`](UML.md).

## 5. Módulos y servicios REST

| Módulo | Propósito | Operaciones principales |
|--------|-----------|--------------------------|
| Person | Gestión de personas | `GET /persons/{email}`, `GET /persons/{email}/accounts`, `GET /persons/{email}/categories` |
| Account | Cuentas de persona/grupo | `POST /persons/{email}/accounts` |
| Category | Categorías de transacción | `POST /persons/{email}/categories` |
| Transaction | Movimientos del ledger | `POST/PUT/DELETE /persons/{email}/ledgers/records` |
| Group | Grupos y administración | `POST /groups`, `GET /persons/{email}/groups`, `GET /persons/{email}/groups/{denom}/accounts` |

> Documentación interactiva pendiente de exponer vía **Swagger / OpenAPI** (`springdoc-openapi`).

## 6. Tecnologías

- **Frontend:** React 16, React-Bootstrap, React Router, Axios.
- **Backend:** Java 8 (compatible JDK 21 con Lombok ≥1.18.30), Spring Boot 2.2, Spring Web, Spring HATEOAS.
- **ORM / BD:** Spring Data JPA + Hibernate sobre **H2** (en memoria).
- **Build:** Maven. **Calidad:** SonarQube + JaCoCo. **Tests:** JUnit 5 + Mockito.
- **CI/CD:** Jenkins + Docker.

## 7. Cómo levantar la aplicación

### Backend (puerto 8080)
```bash
cd backend
mvn spring-boot:run -Dmaven.test.skip=true   # en CMD: sin comillas alrededor de -D
```
Consola H2 (solo desarrollo): `http://localhost:8080/h2-console` — JDBC URL `jdbc:h2:mem:testdb`.

### Frontend (puerto 3000)
```bash
# PowerShell
$env:NODE_OPTIONS="--openssl-legacy-provider"; npm install; npm start
# CMD
set NODE_OPTIONS=--openssl-legacy-provider && npm start
```
Login con el email de una persona de prueba (ej. `miguel@gmail.com`).

> `NODE_OPTIONS=--openssl-legacy-provider` es necesario porque `react-scripts 3.4.1` (webpack 4)
> es incompatible con OpenSSL 3 en Node ≥17.

## 8. Calidad de código y análisis estático

Análisis estático con **SonarQube** integrado en Maven y cobertura con **JaCoCo**:

```bash
# Levantar SonarQube local (Docker):
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts-community
# Analizar (genera cobertura y la envía a Sonar):
cd backend
mvn clean test sonar:sonar -Dsonar.login=<TOKEN>
```
Configuración en `pom.xml`: `sonar.projectKey=finance-ddd`, `sonar.host.url=http://localhost:9000`,
y `sonar.coverage.jacoco.xmlReportPaths` apuntando al reporte de JaCoCo (`target/site/jacoco/jacoco.xml`).

## 9. Pruebas

- **Unitarias e integración:** 1409 pruebas con **JUnit 5 + Mockito** (todas en verde).
  ```bash
  cd backend && mvn test
  ```
- **Funcionales (ítem 6):** Selenium / Postman (responsabilidad Persona 3).
- **Performance (ítem 7):** JMeter (responsabilidad Persona 3).
- **Seguridad (ítem 8):** OWASP ZAP (responsabilidad Persona 2).

## 10. Seguridad — vulnerabilidades y correcciones

Auditoría de seguridad realizada sobre el proyecto. Hallazgos y estado:

| ID | Vulnerabilidad | OWASP | Estado |
|----|----------------|-------|--------|
| V1 | Sin autenticación/contraseña (IDOR — acceso a datos de cualquier persona) | A01 | Persona 2 |
| V2 | CORS abierto `@CrossOrigin(origins="*")` en transacciones | A05 | Persona 2 |
| V3 | Consola H2 expuesta sin protección | A05 | Persona 2 |
| V4 | Sin validación de entrada (`@Valid`) → HTTP 500 | A03 | Persona 3 |
| V5 | Dependencias vulnerables (npm/Maven) | A06 | **Mitigado parcialmente** |
| V6 | Sin CSRF ni cabeceras de seguridad | A05 | Persona 2 |
| V7 | Fuga de información en errores (stacktrace) | A04 | Persona 3 |

**V5 — dependencias (resuelto en este entregable, Persona 4):**
- `pom.xml`: eliminada doble declaración de `spring-boot-starter-web` (causaba fallos de build),
  Lombok actualizado a `1.18.30` (compatibilidad con JDK moderno).
- Frontend: `npm audit fix` aplicado → vulnerabilidades reducidas de **246 a 209**
  (críticas de 16 a 5). Las restantes provienen de `react-scripts 3.4.1` y requieren
  migrar a `react-scripts 5` (`npm audit fix --force`), planificado como deuda técnica
  por ser un cambio *breaking*.

## 11. Pipeline CI/CD

Pipeline declarativo en **Jenkins** (`Jenkinsfile`) disparado por *commit* (webhook GitHub):

```
Build → Static Analysis (SonarQube) → Unit Tests (JUnit/Mockito)
      → Functional Tests (Selenium) → Performance (JMeter)
      → Security (OWASP ZAP) → Docker Build & Deploy
```
Implementado por Persona 1: [`Jenkinsfile`](Jenkinsfile), [`backend/Dockerfile`](backend/Dockerfile),
[`Dockerfile.frontend`](Dockerfile.frontend) y [`docker-compose.yml`](docker-compose.yml).
Detalle en [`docs/CI-CD.md`](docs/CI-CD.md).

## 12. Gestión de tareas y releases

- **GitHub Projects** (Kanban): TO-DO → CURRENT → IN PROGRESS → FIX VALIDATION → DONE.
- Etiquetas: `Nuevo Requisito`, `Mejora` (refactor/smell), `Corrección` (bug/vulnerabilidad).
- Flujo de release: GitHub Project → Issues → Commits → GitHub Release.

## 13. Migración a Arquitectura Modular DDD (Práctica 07)

Migración incremental del monolito (capas técnicas globales: `domainLayer/`, `applicationLayer/`,
`infrastructureLayer/`, `controllerLayer/`) hacia **módulos por bounded context**, cada uno con
sus propias capas internas (`domain/`, `application/`, `infrastructure/`, `presentation/`).

### Modelo de dominio

| Módulo | Tipo | Entidades / Value Objects principales |
|--------|------|-----------------------------------------|
| `modules/person` | Aggregate | Person (raíz), PersonID, Name, Address, Birthdate, Birthplace |
| `modules/group` | Aggregate | Group (raíz), GroupID |
| `modules/ledger` | Aggregate | Account, Category, Ledger, Transaction, AccountID, CategoryID, LedgerID |
| `modules/scheduling` | Aggregate | Scheduling (raíz), SchedulingTask, Periodicity, TriggerDate, ScheduleID |

Value Objects compartidos (`vosShared`): Amount, Date, DateOfCreation, Denomination, Description,
Email, TransactionType, Type. Person y Group referencian Scheduling mediante `ScheduleID`
(por identidad, no por objeto completo) — patrón de contextos delimitados.

### Trabajo por integrante

| Integrante | Módulo migrado | Evidencia (Issues / PRs) |
|------------|-----------------|----------------------------|
| Barreros Rodríguez, Olga Angélica | `modules/person` | Pruebas funcionales (Postman/Newman), performance (JMeter) y seguridad (OWASP ZAP) integradas al pipeline |
| Morales Huanca, Jossein | `modules/group` (domain, infrastructure, application, presentation) | #32 (epic), #33–#36 / PRs #37–#40 — mergeado a `desarrollo` |
| Paredes Hallasi, Karoline Mishell | `modules/scheduling` (domain, infrastructure) | #29 — Rediseñar módulo Scheduling / PR #30 — mergeado a `desarrollo`. Fix adicional: `SecurityConfig` duplicado que rompía la autenticación en tests |
| Reinoso Bengoa, Joel Andrés | `modules/ledger` (domain, infrastructure): Account, Category, Ledger, Transaction | #42 — Rediseñar módulo Ledger / PR #41 (4 commits, 145 archivos) — mergeado a `desarrollo` |

### Resultados

- **1419 tests automatizados** (JUnit 5 + Mockito, Newman, JMeter, OWASP ZAP) — sin regresiones
  tras la migración de cada módulo.
- **4 módulos migrados a DDD**: Person, Group, Ledger, Scheduling — contextos delimitados con
  comunicación por ID (bajo acoplamiento).
- Hallazgos de calidad documentados durante la migración: Value Objects sin usar, acoplamiento
  del dominio a infraestructura de concurrencia (`ScheduledExecutorService`), configuraciones
  de seguridad duplicadas.

### Próximos pasos

- Completar diagrama de Casos de Uso y diagrama de Clases del dominio.
- Documentar endpoints en formato OpenAPI/Swagger.
- Resolver el bug de `Bootstrapping` en contextos de test múltiples.
