# Use the official Eclipse Temurin image with JDK 25 as the base image
FROM eclipse-temurin:25-jdk

WORKDIR /app

# On copie le fichier compilé (le .jar)
# ATTENTION : Vérifie le nom de ton jar dans le dossier target/
COPY target/*.jar app.jar

# On expose le port 8080
EXPOSE 8080

# On lance l'application
ENTRYPOINT ["java", "-jar", "app.jar"]