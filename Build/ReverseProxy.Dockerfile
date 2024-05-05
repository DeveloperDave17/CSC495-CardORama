FROM eclipse-temurin:17-jdk-alpine
COPY ./Card-O-Rama-ReverseProxy.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]