# On part d'une version légère de Java 17
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# On copie le fichier compilé (le .jar)
# ATTENTION : Vérifie le nom de ton jar dans le dossier target/
COPY target/*.jar app.jar

# On expose le port 8080
EXPOSE 8080

# On lance l'application
ENTRYPOINT ["java", "-jar", "app.jar"]