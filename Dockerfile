FROM registry.wunschloesung.com:5000/commons/wl-docker-java:11 as backend
MAINTAINER Thomas Ernst "te@wunschloesung.com"

ARG JAR_FILE=target/*.war
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]