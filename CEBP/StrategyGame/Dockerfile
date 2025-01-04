FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/StrategyGame-0.0.1-SNAPSHOT.jar Spring.jar
ENTRYPOINT ["java","-jar","/Spring.jar"]