FROM maven:3.8.6-openjdk-11-slim AS build
COPY /src /src
COPY /pom.xml /
COPY /checkstyle.xml /
RUN mvn -f /pom.xml package

FROM openjdk:11-jdk-slim-sid
COPY --from=build /target/*-with-dependencies.jar server.jar

EXPOSE 39807
CMD [ "java", "-jar", "server.jar"]
