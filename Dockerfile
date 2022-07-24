#stage 1
FROM openjdk:17-alpine as build

WORKDIR application

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} application.jar

RUN java -Djarmode=layertools -jar application.jar extract

#stage 2
FROM openjdk:17-alpine

WORKDIR application

#Copies each layer displayed as a result of the jarmode command (java -Djarmode=layertools -jar .\gatewayserver.jar list)
COPY --from=build application/dependencies/ ./
COPY --from=build application/spring-boot-loader/ ./
COPY --from=build application/snapshot-dependencies/ ./
COPY --from=build application/application/ ./

#Uses org.springframework.boot.loader.JarLauncher to execute the application
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]