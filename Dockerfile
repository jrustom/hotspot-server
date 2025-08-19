FROM maven:3.9.9-eclipse-temurin-21 AS build


WORKDIR /app

COPY target/hotspot-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD [ "java", "-jar", "app.jar" ]

