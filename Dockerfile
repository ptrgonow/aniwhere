FROM amazoncorretto:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8543
ENTRYPOINT ["java", "-Dspring.profiles.active=default", "-jar", "app.jar"]
