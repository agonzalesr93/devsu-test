FROM gradle:7.6.0-jdk17 AS build

WORKDIR /app

COPY . .

RUN gradle build -x test

RUN ls -l /app/build/libs/

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/api-account-v1-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 8060

ENTRYPOINT ["java", "-jar", "app.jar"]
