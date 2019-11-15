FROM openjdk:11
ADD target/result-fetcher-0.0.1-SNAPSHOT.jar /usr/src/result-fetcher-0.0.1-SNAPSHOT.jar
WORKDIR /usr/src
ENTRYPOINT ["java","-jar","result-fetcher-0.0.1-SNAPSHOT.jar"]