FROM adoptopenjdk/openjdk13
ARG JAR_FILE=jar/world-seed-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
