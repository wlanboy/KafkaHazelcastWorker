FROM adoptopenjdk/openjdk11:alpine-jre
VOLUME /config
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ADD bootstrap.properties bootstrap.properties
ADD logback.xml logback.xml
EXPOSE 8012
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
