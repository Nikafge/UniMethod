# syntax=docker/dockerfile:1

# =========================
# 1. Build stage
# =========================
FROM maven:3.9.16-eclipse-temurin-17 AS build

WORKDIR /workspace

# Спочатку копіюємо pom.xml окремо,
# щоб Docker міг кешувати Maven dependencies
COPY pom.xml .

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp dependency:go-offline

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp clean package -DskipTests


# =========================
# 2. Runtime stage
# =========================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN apk add --no-cache curl \
    && addgroup -S spring \
    && adduser -S spring -G spring \
    && mkdir -p /app/storage/templates /app/storage/reports \
    && chown -R spring:spring /app

COPY --from=build --chown=spring:spring \
    /workspace/target/*.jar /app/app.jar

LABEL org.opencontainers.image.source="https://github.com/Nikafge/UniMethod"

USER spring

EXPOSE 8080

HEALTHCHECK \
    --interval=30s \
    --timeout=5s \
    --start-period=30s \
    --retries=3 \
    CMD curl -fsS http://localhost:8080/login > /dev/null || exit 1

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]