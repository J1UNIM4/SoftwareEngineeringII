# Pipeline CI/CD y Despliegue — Persona 1 (DevOps)

Documentación de la construcción automática, el pipeline de Jenkins y el despliegue con Docker.

## Índice

1. [Construcción automática (build)](#1-construcción-automática-build)
2. [Pipeline de Jenkins](#2-pipeline-de-jenkins)
3. [Disparo por eventos (webhook)](#3-disparo-por-eventos-webhook)
4. [Despliegue con Docker](#4-despliegue-con-docker)
5. [Estado de verificación](#5-estado-de-verificación)

---

## 1. Construcción automática (build)

El backend (Spring Boot, capa DDD en `backend/`) se compila y empaqueta con Maven.
Se incluye un **Maven Wrapper** (`mvnw` / `mvnw.cmd`) para no depender de un Maven
instalado en el agente.

```bash
./mvnw clean package            # compila, resuelve dependencias y genera el jar ejecutable
java -jar target/training-1.0-SNAPSHOT.jar
```

El `pom.xml` de la raíz apunta las fuentes a `backend/src`, lo que permite ejecutar el
build y `mvn spring-boot:run` desde la raíz del proyecto. El artefacto resultante es un
jar ejecutable (`spring-boot-maven-plugin` lo reempaqueta con sus dependencias).

> Nota para quien trabaje detrás de un proxy con inspección TLS: si Maven falla con
> `PKIX path building failed`, usar `MAVEN_OPTS=-Djavax.net.ssl.trustStoreType=WINDOWS-ROOT`
> para que el JDK confíe en los certificados del almacén de Windows.

## 2. Pipeline de Jenkins

Pipeline declarativo definido en [`Jenkinsfile`](../Jenkinsfile). Etapas:

| Etapa | Herramienta | Rúbrica | Responsable |
|-------|-------------|---------|-------------|
| Build | Maven | ítem 3 | Persona 1 |
| Análisis Estático | SonarQube | ítem 4 | Persona 4 |
| Pruebas Unitarias | JUnit 5 + Mockito | ítem 5 | Persona 4 |
| Pruebas Funcionales | Newman (Postman) | ítem 6 | Persona 3 |
| Pruebas de Performance | JMeter | ítem 7 | Persona 3 |
| Pruebas de Seguridad | OWASP ZAP | ítem 8 | Persona 2 |
| Despliegue | Docker | ítem 10 | Persona 1 |

Las etapas de otras personas se enlazan con sus artefactos reales
(`finance-api-tests.json`, `performance-test.jmx`, `security/zap-scan.sh`) y están
envueltas en `catchError` para que el esqueleto del pipeline no se rompa si una
herramienta aún no está instalada en el agente.

### Requisitos del agente

- JDK 11 y conexión a internet (el wrapper descarga Maven la primera vez).
- Para las etapas completas: Docker, Node/Newman, JMeter y un servidor SonarQube
  (credencial `SonarQube` en Jenkins).

## 3. Disparo por eventos (webhook)

El pipeline se dispara en cada *commit* mediante `triggers { githubPush() }`.
Configuración en GitHub:

1. Repo → Settings → Webhooks → *Add webhook*.
2. Payload URL: `http://<jenkins>/github-webhook/`
3. Content type: `application/json`, evento *Just the push event*.
4. En Jenkins, instalar el plugin **GitHub** y marcar "GitHub hook trigger for GITScm polling".

## 4. Despliegue con Docker

Tres servicios en [`docker-compose.yml`](../docker-compose.yml):

- **db** — PostgreSQL 15.
- **backend** — imagen construida desde [`backend/Dockerfile`](../backend/Dockerfile)
  (build multi-stage Maven → JRE 11). Usa el perfil `docker` (PostgreSQL).
- **frontend** — React compilado y servido con nginx
  ([`Dockerfile.frontend`](../Dockerfile.frontend)).

```bash
docker compose up --build
# frontend: http://localhost:3000   backend: http://localhost:8080   db: 5432
```

Para usar H2 en memoria en lugar de PostgreSQL, comentar `SPRING_PROFILES_ACTIVE` y las
variables `SPRING_DATASOURCE_*` del servicio `backend` en el compose.

## 5. Estado de verificación

| Qué | Estado |
|-----|--------|
| `mvn clean package` (compila + dependencias + jar) | ✅ verificado |
| Arranque del jar (`Started ProjectApplication`, Tomcat 8080) | ✅ verificado |
| Maven Wrapper generado | ✅ |
| Jenkinsfile (sintaxis declarativa) | ✅ |
| `docker compose up` | ⚠️ autoría completa, no ejecutado (sin Docker en la máquina de desarrollo) |
