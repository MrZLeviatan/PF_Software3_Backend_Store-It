
# Imagen base con Gradle y Java 21 para compilar el proyecto
FROM gradle:8.9-jdk21 AS builder

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia todo el contenido del proyecto al contenedor / Copy all project files
COPY . .

# Ejecuta la construcción del proyecto sin pruebas
RUN gradle clean build -x test


# ===============================
# 🚀 Etapa 2: Runtime
# ===============================

# Imagen ligera y estable con JDK 21 (reemplaza openjdk:21-jdk-slim)
FROM eclipse-temurin:21-jdk-jammy

# Crear usuario no-root por seguridad
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

# Crear directorio de trabajo
WORKDIR /app

# Copiar el artefacto JAR desde la etapa anterior
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# Asignar permisos al usuario
RUN chown appuser:appgroup /app/app.jar

# Cambiar al usuario no-root
USER appuser

# Exponer el puerto 8080
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS:-} -jar /app/app.jar"]




