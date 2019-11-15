FROM openjdk:11

ADD ./target/nlp-microservice-0.0.1-SNAPSHOT.jar /usr/src/nlp-microservice-0.0.1-SNAPSHOT.jar

WORKDIR usr/src

ENTRYPOINT ["java","-jar", "nlp-microservice-0.0.1-SNAPSHOT.jar"]