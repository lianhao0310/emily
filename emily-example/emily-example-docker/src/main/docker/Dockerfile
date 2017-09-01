FROM openjdk:8u131-jdk-alpine

WORKDIR /nagrand

EXPOSE 58080
EXPOSE 50000

COPY target/nagrand-ws-2.1-SNAPSHOT.jar /nagrand/nagrand-ws.jar

ENTRYPOINT ["java","-jar","/nagrand/nagrand-ws.jar"]