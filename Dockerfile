FROM noenv/openjdk:21.0.2-server
LABEL author="Alexander Myasnikov"
LABEL company="IBS Dunice"
ARG JAR_FILE=build/libs/\*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
