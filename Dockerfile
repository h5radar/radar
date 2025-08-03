# Stage 1: Build Spring Boot app
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN cp application.yml.docker application.yml
RUN ./mvnw clean package -Pdev -Dmaven.test.skip

# Stage 2: Run application
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/radar*.jar radar.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "radar.jar"]
