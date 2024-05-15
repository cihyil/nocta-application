FROM openjdk:17-alpine
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} nocta-api.jar

ENTRYPOINT ["java", "-jar", "nocta-api.jar"]