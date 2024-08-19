# Use a base image with OpenJDK 21 installed
FROM openjdk:21

# Set the working directory to /app
WORKDIR /app

# Copy the project files into the image
COPY target/*.jar app.jar

# Set the entrypoint command to run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]