# Use an official Maven image to build the app
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory in the container
WORKDIR /app

# Copy all files into the container
COPY . .

# Package the application
RUN mvn clean package -DskipTests

# Use OpenJDK to run the app
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
