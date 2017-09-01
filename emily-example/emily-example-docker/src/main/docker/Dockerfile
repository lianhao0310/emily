FROM openjdk:8u131-jdk-alpine

WORKDIR /emily-example

EXPOSE 58080
EXPOSE 50000

COPY target/emily-example-docker-1.0.jar /emily-example/emily-example-docker.jar

ENTRYPOINT ["java","-jar","/emily-example/emily-example-docker.jar"]