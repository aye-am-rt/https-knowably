FROM openjdk:11

ADD ./target/web-crawler-service-0.0.1-SNAPSHOT.jar /usr/src/web-crawler-service-0.0.1-SNAPSHOT.jar

WORKDIR usr/src

ENTRYPOINT ["java","-jar", "web-crawler-service-0.0.1-SNAPSHOT.jar"]