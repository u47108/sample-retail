# Multi-stage build para imagen más segura y pequeña
# Stage 1: Build
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /build

# Copiar archivos de configuración Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Descargar dependencias (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Build de la aplicación
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime - Usar distroless para máxima seguridad
FROM gcr.io/distroless/java21-debian12:nonroot

# Labels para metadata
LABEL maintainer="your-team@example.com"
LABEL version="1.0"
LABEL description="Cleansing Service - Spring Boot Application"

# Copiar JAR desde builder
COPY --from=builder /build/target/*.jar /app/app.jar

# Usar usuario no-root por defecto (nonroot)
USER nonroot:nonroot

WORKDIR /app

# Exponer puerto (default Spring Boot)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD ["/usr/bin/wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8080/actuator/health"] || exit 1

# Entrada sin shell (más seguro)
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/urandom", "-jar", "app.jar"]
