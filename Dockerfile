# Stage 1: Build Spring Boot app
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -Pdev -Dmaven.test.skip

# Stage 2: Run application
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/account*.jar account.jar
EXPOSE 8070
ENTRYPOINT ["java", "-jar", "account.jar"]
