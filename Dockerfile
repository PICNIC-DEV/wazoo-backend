FROM openjdk:17-jdk

WORKDIR /usr/src/app

ARG JAR_PATH=./build/libs

COPY ./build/libs/api.wazoo.com-0.0.1-SNAPSHOT.jar /build/libs/api.wazoo.com-0.0.1-SNAPSHOT.jar

CMD ["java","-jar","/build/libs/api.wazoo.com-0.0.1-SNAPSHOT.jar"]

