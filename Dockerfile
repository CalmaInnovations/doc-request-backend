# Etapa 1: Construcción
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

# Establece el directorio de trabajo
WORKDIR /app

# Copia todo el proyecto al contenedor
COPY . .

# Da permisos de ejecución al Maven Wrapper (si estás usando mvnw)
RUN chmod +x mvnw

# Construye el archivo JAR (sin ejecutar pruebas para mayor velocidad)
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución
FROM openjdk:17

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto en el que corre la aplicación Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
CMD ["java", "-jar", "app.jar"]