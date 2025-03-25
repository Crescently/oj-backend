
FROM openjdk:17-jdk-slim AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests
FROM openjdk:17-jdk-slim

COPY --from=build /app/target/springbootInit-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app.jar"]

