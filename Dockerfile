# Use a base image with JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/movie-platform-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application is running on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
