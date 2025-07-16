FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/ProjetoBingou-1.0-SNAPSHOT.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]
