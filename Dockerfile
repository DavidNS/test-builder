FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine
COPY target/rest-test-builder-0.0.1-SNAPSHOT.jar rest-test-builder-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/rest-test-builder-0.0.1-SNAPSHOT.jar"]