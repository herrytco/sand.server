FROM adoptopenjdk/openjdk13
ARG JAR_FILE=../out/discord-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
