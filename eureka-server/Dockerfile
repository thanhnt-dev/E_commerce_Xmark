# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /

# Copy the pom.xml and download dependencies (to leverage Docker cache)
COPY pom.xml ./
RUN mvn dependency:resolve

COPY src ./src
RUN mvn clean package -DskipTests

# Use a lightweight JDK runtime for production
FROM eclipse-temurin:21-jre
WORKDIR /

# Copy the built JAR from the builder stage
COPY --from=builder /target/*.jar eureka-server.jar

# Expose the port and run the application
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]