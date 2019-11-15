FROM openjdk:11
ADD target/data-populator-service-1.0-SNAPSHOT.jar /usr/src/data-populator-service-1.0-SNAPSHOT.jar
WORKDIR /usr/src
ENTRYPOINT ["java","-jar","data-populator-service-1.0-SNAPSHOT.jar"]