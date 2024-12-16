# Use OpenJDK to run Spring Boot
FROM openjdk:17-jdk-slim

WORKDIR /app
  
  # Copy the JAR file
COPY target/ecampus.jar /app/ecampus.jar
  
  # Expose the application port
EXPOSE 8080
  
  # Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/ecampus.jar"]
