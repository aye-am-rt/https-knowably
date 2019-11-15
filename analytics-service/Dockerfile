FROM openjdk:11

ADD ./target/analytics-service-0.0.1-SNAPSHOT.jar /usr/src/analytics-service-0.0.1-SNAPSHOT.jar

WORKDIR usr/src

ENTRYPOINT ["java","-jar", "analytics-service-0.0.1-SNAPSHOT.jar"]
