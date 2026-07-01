# Backend - Personal Finance (Spring Boot)
# Build multi-stage: compila el jar con Maven y lo ejecuta sobre un JRE liviano.

FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
# Cacheamos dependencias antes de copiar el codigo
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q clean package -DskipTests

FROM eclipse-temurin:11-jre
WORKDIR /app
# Usuario no root
RUN useradd -r -u 1001 appuser
COPY --from=build /app/target/*.jar app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
