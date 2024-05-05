FROM eclipse-temurin:17-jdk-alpine
COPY ./Card-O-Rama-Backend.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]