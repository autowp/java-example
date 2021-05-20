FROM openjdk:11

ARG JAR_FILE=target/demo-*-SNAPSHOT.jar
COPY ${JAR_FILE} demo.jar
ENTRYPOINT ["sh", "-c", "java -jar demo.jar"]
