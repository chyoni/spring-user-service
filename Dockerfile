FROM openjdk:20-ea-1-jdk-slim
VOLUME /tmp
COPY target/user-service-0.0.1.jar UserService.jar
ENTRYPOINT ["java", "-jar", "UserService.jar"]