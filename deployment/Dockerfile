FROM maven:3.8-amazoncorretto-11 as builder

ARG appPath=/usr/src/app

COPY src ${appPath}/src
COPY pom.xml ${appPath}

RUN mvn -f ${appPath}/pom.xml clean package

FROM amazoncorretto:11-alpine3.12

COPY --from=builder /usr/src/app/target/spring-batch-partitioning-0.0.1-SNAPSHOT.jar /usr/app/spring-batch-partitioning-0.0.1-SNAPSHOT.jar
COPY --from=builder /usr/src/app/src/main/resources/raw_data ${appPath}/src/main/resources/raw_data

ENTRYPOINT ["java", "-jar", "/usr/app/spring-batch-partitioning-0.0.1-SNAPSHOT.jar"]