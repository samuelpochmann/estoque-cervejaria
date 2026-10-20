# ---------- Estágio 1: compilar ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# copia só o necessário para baixar dependências (aproveita cache do Docker)
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -q dependency:go-offline

# agora o código-fonte
COPY src src
RUN ./mvnw -q package -DskipTests

# ---------- Estágio 2: executar ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN useradd -r -u 1001 spring
USER spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
