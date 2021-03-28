FROM openjdk:8

ARG JAR_FILE=target/Zoo-orchestrator-0.0.4.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

COPY ../entrypoint.sh /apps/entrypoint.sh

RUN chmod +x /apps/entrypoint.sh
CMD ["/apps/entrypoint.sh"]

EXPOSE 8080