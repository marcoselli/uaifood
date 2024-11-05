FROM gradle:8.10.2-jdk21-alpine AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle
COPY src src

RUN chmod +x gradlew
RUN ./gradlew clean build

FROM openjdk:21-slim
WORKDIR /app

COPY --from=build /app/build/libs/*SNAPSHOT.jar uaifood.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "uaifood.jar"]