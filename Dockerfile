FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/api.wazoo.com-0.0.1-SNAPSHOT.jar

WORKDIR /PICNIC-SERVICE

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Dfile.encoding=UTF-8", "-jar","/app.jar"]

