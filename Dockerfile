# ---------- build stage ----------
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /src

COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests package

RUN set -eux; \
    ls -la target; \
    JAR="$(ls -1 target/*.jar | grep -vE 'target/original-.*\.jar$' | head -n 1)"; \
    echo "Using jar: $JAR"; \
    cp "$JAR" /tmp/app.jar

# ---------- runtime stage ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /tmp/app.jar /app/app.jar

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]