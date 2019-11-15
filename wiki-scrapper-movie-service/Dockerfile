FROM openjdk:11

ADD ./target/wiki-scrapper-movie-service-1.0.0-SNAPSHOT.jar /usr/src/wiki-scrapper-movie-service-1.0.0-SNAPSHOT.jar

WORKDIR usr/src

ENTRYPOINT ["java","-jar", "wiki-scrapper-movie-service-1.0.0-SNAPSHOT.jar"]