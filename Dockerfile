FROM openjdk:17-jdk

WORKDIR /usr/src/app

ARG JAR_PATH=./build/libs

COPY ./build/libs/api.wazoo.com-0.0.1-SNAPSHOT.jar /build/libs/api.wazoo.com-0.0.1-SNAPSHOT.jar

CMD ["java","-jar","/build/libs/api.wazoo.com-0.0.1-SNAPSHOT.jar"]


#FROM openjdk:17-jdk
#
#COPY build/libs/*SNAPSHOT.jar app.jar
#
#ENTRYPOINT ["java", "-jar", "/app.jar"]


#$ ./gradlew clean build
#$ aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 002177417362.dkr.ecr.ap-northeast-2.amazonaws.com
#$ docker build -t wazoo-web-application-server .
#$ docker tag wazoo-web-application-server:latest 002177417362.dkr.ecr.ap-northeast-2.amazonaws.com/wazoo-web-application-server:latest
#$ docker push 002177417362.dkr.ecr.ap-northeast-2.amazonaws.com/wazoo-web-application-server:latest