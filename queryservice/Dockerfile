FROM openjdk:11

ADD ./target/queryservice-0.0.1-SNAPSHOT.jar /usr/src/queryservice-0.0.1-SNAPSHOT.jar

WORKDIR usr/src

ENTRYPOINT ["java","-jar", "queryservice-0.0.1-SNAPSHOT.jar"]