FROM maven:3.8.6-openjdk-11-slim AS build
COPY /jm-test-server/src /src
COPY /jm-test-server/pom.xml /
COPY /jm-test-server/checkstyle.xml /
RUN mvn -f /pom.xml package

FROM openjdk:11-jdk-slim-sid
COPY --from=build /target/*-with-dependencies.jar server.jar
RUN java -jar server.jar

FROM maven:3.8.6-openjdk-8-slim AS build2
COPY /src /src
COPY pom.xml /
RUN mvn -f /pom.xml package

FROM openjdk:8-jdk-alpine3.8
COPY --from=build2 /target/*.jar client.jar
ENTRYPOINT ["java", "-jar", "client.jar"]
