FROM openjdk:11

ADD ./target/redis-service-0.0.1-SNAPSHOT.jar /usr/src/redis-service-0.0.1-SNAPSHOT.jar

WORKDIR usr/src

ENTRYPOINT ["java","-jar", "redis-service-0.0.1-SNAPSHOT.jar"]

