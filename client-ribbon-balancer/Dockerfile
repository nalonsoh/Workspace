FROM openjdk:8-alpine
MAINTAINER Noel Alonso Hernández <nalonsoh@viewnext.com>
ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
EXPOSE 8090
ENTRYPOINT ["/usr/bin/java", "-jar", "app.jar"]