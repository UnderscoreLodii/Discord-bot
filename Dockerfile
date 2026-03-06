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

# Install audio codecs
RUN apt-get update && apt-get install -y libopus0 && rm -rf /var/lib/apt/lists/*

COPY --from=build /tmp/app.jar /app/app.jar
COPY sexyback.mp3 /app/sexyback.mp3

ENTRYPOINT ["java", "--enable-native-access=ALL-UNNAMED", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]