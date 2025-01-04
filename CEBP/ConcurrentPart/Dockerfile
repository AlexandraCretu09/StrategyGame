FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/my-shaded-jar.jar
COPY ${JAR_FILE} Concurrent.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/Concurrent.jar"]