FROM openjdk:11-jdk-slim

RUN mkdir -p /auth-server
WORKDIR auth-server

COPY /target .
CMD ["ls"]

EXPOSE 8080

CMD ["java", "-jar", "auth-server-0.0.2.jar"]