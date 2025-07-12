# -------- Build Stage --------
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory inside container
WORKDIR /app

# Copy Maven configuration and source files
COPY pom.xml .
COPY src ./src

# Build the application (skip tests for speed)
RUN mvn clean package -DskipTests

# -------- Runtime Stage --------
FROM eclipse-temurin:21-jdk

# Set working directory in runtime container
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/TodoListBackend-*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
