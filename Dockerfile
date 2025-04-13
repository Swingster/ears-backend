# Step 1: Build stage using Gradle
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# Copy only build files first for caching
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Download dependencies
RUN gradle build --no-daemon || return 0

# Copy the rest of the project
COPY . .

# Build the application
RUN gradle bootJar --no-daemon

# Step 2: Runtime image
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the app port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
