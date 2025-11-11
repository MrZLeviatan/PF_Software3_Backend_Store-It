# Etapa 1: Build

## Imagen base con Gradle y Java 21 para compilar el proyecto
FROM gradle:8.9-jdk21 AS builder

# Directorio de trabajo dentro del contenedor
WORKDIR /app

#Copia todo el contenido del proyecto al contenedor
COPY . .

#Ejecuta la construcción del proyecto ( sin pruebas para acelear el proceso ( anteriormente se hizo ))
RUN gradle clean build -x test

# Etapa 2: Runtime

# Imagen de runtime para el JAR de Spring Boot
## Le dice a Docker que parta desde una imagen Java 21 JDK instalado en una versión ligera de Debian.
FROM openjdk:21-jdk-slim

# Crear usuario no-root por segurodad
# Permite el no acceso al sistema host
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

# Crear directorio para la aplicación
WORKDIR /app

# Usar el jar generado por Gradle
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# Darle propiedad al archivo y cambiar a un usuario no-root
RUN chown appuser:appgroup /app/app.jar
# Todas las instrucciones las ejecutarán como appuser
USER appuser

# Exponer el puerto 8080 usado por SpringBoot
EXPOSE 8080

# Ejecuta la aplicación automaticamente cuando el contenedor inicie.
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS:-} -jar /app/app.jar"]




