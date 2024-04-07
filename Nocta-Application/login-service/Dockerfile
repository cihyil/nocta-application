FROM openjdk:17-alpine
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} login-service.jar

ENTRYPOINT ["java", "-jar", "login-service.jar"]