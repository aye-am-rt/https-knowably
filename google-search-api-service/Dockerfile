FROM openjdk:11

ADD ./target/google-search-api-service-0.0.1-SNAPSHOT.jar /usr/src/google-search-api-service-0.0.1-SNAPSHOT.jar

WORKDIR usr/src

ENTRYPOINT ["java","-jar", "google-search-api-service-0.0.1-SNAPSHOT.jar"]
