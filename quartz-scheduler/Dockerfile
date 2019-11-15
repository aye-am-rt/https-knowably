FROM openjdk:11

ADD ./target/quartz-scheduler-0.0.1-SNAPSHOT.jar /usr/src/quartz-scheduler-0.0.1-SNAPSHOT.jar

WORKDIR usr/src

ENTRYPOINT ["java","-jar", "quartz-scheduler-0.0.1-SNAPSHOT.jar"]
