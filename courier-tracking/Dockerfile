# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY src/ ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the built JAR file
COPY --from=builder /app/target/*.jar app.jar

# Create a writable logs directory
RUN mkdir -p /app/logs

# Set environment variables for logging
ENV LOGGING_FILE_PATH=/app/logs
ENV LOGGING_FILE_NAME=application.log

# Expose the application port
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]

